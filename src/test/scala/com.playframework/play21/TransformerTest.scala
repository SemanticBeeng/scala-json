package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.JsPath.json._
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import AbstractExample.{json}

class TransformerTestSuite extends FunSuite {

  test("Pick JSON value in JsPath") {
    val jsonTransformer = (__ \ 'key2 \ 'key23).json.pick
    val actualResult = json.validate(jsonTransformer)
    val expectedResult = JsSuccess(Json.arr("alpha", "beta", "gamma"), (__ \ 'key2 \ 'key23))
    assert(actualResult === expectedResult)
  }

  test("Pick value as Type") {
    val jsonTransformer = (__ \ 'key2 \ 'key23).json.pick[JsArray]
    val actualResult = json.validate(jsonTransformer)
    val expectedResult = JsSuccess(Json.arr("alpha", "beta", "gamma"), (__ \ 'key2 \ 'key23))
    assert(actualResult === expectedResult)
  }

  test("Pick branch following JsPath") {
    val jsonTransformer = (__ \ 'key2 \ 'key24 \ 'key241).json.pickBranch
    val actualResult = json.validate(jsonTransformer).get
    val expectedResult = Json.obj("key2" -> Json.obj("key24" -> Json.obj("key241" -> JsNumber(234.123))))
    assert(actualResult === expectedResult)
  }

  test("Copy a value from input JsPath into a new JsPath") {
    val jsonTransformer = (__ \ 'key25 \ 'key251).json.copyFrom((__ \ 'key2 \ 'key21).json.pick)
    val actualResult = json.validate(jsonTransformer)
    val expectedResult = JsSuccess(Json.obj("key25" -> Json.obj("key251" -> JsNumber(123))), (__ \ 'key2 \ 'key21))
    assert(actualResult === expectedResult)
  }

  test("Copy full input Json & update a branch") {
    val jsonTransformer = (__ \ 'key2 \ 'key24).json.update(
      __.read[JsObject].map { o => o ++ Json.obj("field243" -> "coucou") }
    )
    val actualResult = json.validate(jsonTransformer)
    val expectedJson = Json.obj(
      "key1" -> "value1",
      "key2" -> Json.obj(
        "key21" -> 123,
        "key22" -> true,
        "key23" -> Json.arr("alpha", "beta", "gamma"),
        "key24" -> Json.obj(
          "key241" -> 234.123,
          "key242" -> "value242",
          "field243" -> "coucou"
        )
      ),
      "key3" -> 234
    )
    val expectedResult = JsSuccess(expectedJson)
    assert(actualResult === expectedResult)
  }

  test("Put a given value in a new branch") {
    val jsonTransformer = (__ \ 'key24 \ 'key241).json.put(JsNumber(456))
    val actualResult = json.validate(jsonTransformer)
    val expectedResult = JsSuccess(Json.obj("key24" -> Json.obj("key241" -> JsNumber(456))))
    assert(actualResult === expectedResult)
  }

  // Please note the resulting JsObject hasn’t same keys order as input JsObject.
  // This is due to the implementation of JsObject and to the merge mechanism.
  // But this is not important since we have overriden JsObject.equals method to
  // take this into account.
  test("Prune a branch from input JSON") {
    val jsonTransformer = (__ \ 'key2 \ 'key22).json.prune
    val actualResult = json.validate(jsonTransformer)
    val expectedJson = Json.obj(
      "key1" -> "value1",
      "key2" -> Json.obj(
        "key21" -> 123,
        "key23" -> Json.arr("alpha", "beta", "gamma"),
        "key24" -> Json.obj(
          "key241" -> 234.123,
          "key242" -> "value242"
        )
      ),
      "key3" -> 234
    )
    val expectedResult = JsSuccess(expectedJson, (__ \ 'key2 \ 'key22 \ 'key22))
    assert(actualResult === expectedResult)
  }

  test("Pick a branch and update its content in 2 places") {
    val jsonTransformer = (__ \ 'key2).json.pickBranch(
      (__ \ 'key21).json.update(
        of[JsNumber].map { case JsNumber(nb) => JsNumber(nb + 10) }
      ) andThen
        (__ \ 'key23).json.update(
          of[JsArray].map { case JsArray(arr) => JsArray(arr :+ JsString("delta")) }
        )
    )
    val actualResult = json.validate(jsonTransformer)
    val expectedJson = Json.obj(
      "key2" -> Json.obj(
        "key21" -> 133,
        "key22" -> true,
        "key23" -> Json.arr("alpha", "beta", "gamma", "delta"),
        "key24" -> Json.obj(
          "key241" -> 234.123,
          "key242" -> "value242"
        )
      )
    )
    val expectedResult = JsSuccess(expectedJson, (__ \ 'key2))
    assert(actualResult === expectedResult)
  }

  test("Pick a branch and prune a sub-branch") {
    val jsonTransformer = (__ \ 'key2).json.pickBranch(
      (__ \ 'key23).json.prune
    )

    val actualResult = json.validate(jsonTransformer)
    val expectedJson = Json.obj(
      "key2" -> Json.obj(
        "key21" -> 123,
        "key22" -> true,
        "key24" -> Json.obj(
          "key241" -> 234.123,
          "key242" -> "value242"
        )
      )
    )
    val expectedResult = JsSuccess(expectedJson, (__ \ 'key2 \ 'key23))
    assert(actualResult === expectedResult)
  }

   test("Convert Gizmo to Gremlin") {
    val gizmo = Json.obj(
      "name" -> "gizmo",
      "description" -> Json.obj(
        "features" -> Json.arr("hairy", "cute", "gentle"),
        "size" -> 10,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> Json.obj(
          "wet" -> "multiplies",
          "feed after midnight" -> "becomes gremlin"
        )
      ),
      "loves" -> "all"
    )
    val gizmo2gremlin = (
      (__ \ 'name).json.put(JsString("gremlin")) and
      (__ \ 'description).json.pickBranch(
        (__ \ 'size).json.update(of[JsNumber].map { case JsNumber(size) => JsNumber(size * 3) }) and
          (__ \ 'features).json.put(Json.arr("skinny", "ugly", "evil")) and
          (__ \ 'danger).json.put(JsString("always")) reduce
      ) and
        (__ \ 'hates).json.copyFrom((__ \ 'loves).json.pick)
    ) reduce
    val actualResult = gizmo.validate(gizmo2gremlin)
    val expectedResult = JsSuccess(Json.obj(
      "name" -> "gremlin",
      "description" -> Json.obj(
        "features" -> Json.arr("skinny", "ugly", "evil"),
        "size" -> 30,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> "always"
      ),
      "hates" -> "all"
    ))
    assert(actualResult === expectedResult)
  }


}