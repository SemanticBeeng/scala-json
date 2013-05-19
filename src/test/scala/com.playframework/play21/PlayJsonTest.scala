package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json._
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.data.validation.ValidationError

class PlayJsonTestSuite extends FunSuite {

  test("Object parsed from JSON equals directly created object") {
    val createdKoala = Koala("kaylee", EucalyptusTree(10, 23))
    val parsedKoala = Json.fromJson[Koala](
      Json.obj(
        "name" -> "kaylee",
        "home" -> Json.obj(
          "col" -> 10,
          "row" -> 23
        )
      )
    ).get

    assert(createdKoala === parsedKoala)
  }

  test("Create JSON Syntax") {

    val stringifiedJSON = Json.obj(
      "key1" -> "value1",
      "key2" -> 234,
      "key3" -> Json.obj(
        "key31" -> true,
        "key32" -> Json.arr("alpha", "beta", 234.13),
        "key33" -> Json.obj("key1" -> "value2", "key34" -> "value34")
      )
    ).toString()

    assert(stringifiedJSON === "{\"key1\":\"value1\",\"key2\":234,\"key3\":{\"key31\":true,\"key32\":[\"alpha\",\"beta\",234.13],\"key33\":{\"key1\":\"value2\",\"key34\":\"value34\"}}}")
  }

  test("Navigating JSON using JsPath") {

    val json = Json.obj(
      "key1" -> "value1",
      "key2" -> 234,
      "key3" -> Json.obj(
        "key31" -> true,
        "key32" -> Json.arr("alpha", "beta", 234.13),
        "key33" -> Json.obj("key1" -> "value2", "key34" -> "value34")
      )
    )

    val stuff = (__ \ "key1")

    val jsPath = (__ \ "key1")(json)
    assert(jsPath === List(JsString("value1")))

    val twoLevelPath = (__ \ "key3" \ "key33")(json)
    assert(twoLevelPath === List(Json.obj("key1" -> "value2", "key34" -> "value34")))

    val indexedPath = (__ \ "key3" \ "key32")(2)(json)
    assert(indexedPath === List(JsNumber(234.13)))

    val multiplePaths = (__ \\ "key1")(json)
    assert(multiplePaths === List(JsString("value1"), JsString("value2")))
  }

  test("JSON reads success for creature") {

    val json = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F
    )
    val validatedCreature = json.validate[Creature]

    val successCreature = JsSuccess(Creature("gremlins", false, 1.0F))

    assert(validatedCreature === successCreature)
  }

  test("JSON reads fails with single validation error for creature") {

    val json = Json.obj(
      "name" -> "gremlins",
      "weight" -> 1.0F
    )
    val validatedCreature = json.validate[Creature]

    val error = JsError(__ \ 'isDead, ValidationError("validate.error.missing-path"))

    assert(validatedCreature === error)
  }

  test("Folding on successful JSON reads for creature") {

    val json = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F
    )
    val readSuccess: JsResult[Creature] = json.validate[Creature]

    val foldResult = readSuccess.fold(
      valid = {
        c => c.name
      },
      invalid = {
        e => e
      }
    )

    assert(foldResult === "gremlins")
  }

  test("Folding on failed JSON reads for creature") {

    val json = Json.obj(
      "name" -> "gremlins",
      "weight" -> 1.0F
    )
    val readFailed: JsResult[Creature] = json.validate[Creature]

    val foldResult = readFailed.fold(
      valid = {
        c => c.name
      },
      invalid = {
        e => e
      }
    )

    // its a list if tuples on a path with a corresponding list of validation errors)
    val validationErrors = List((__ \ 'isDead, List(ValidationError("validate.error.missing-path"))))

    assert(foldResult === validationErrors)
  }

  test("Read single complex creature successfully") {

    val gizmoJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj("string" -> "alpha", "number" -> 85),
      "friends" -> Json.arr(),
      "social" -> "@gizmo"
    )
    val gizmoCreature = gizmoJson.validate[ComplexCreature]

    gizmoCreature match {
      case s: JsSuccess[ComplexCreature] => assert(true)
      case _ => fail("should be JsSuccess")
    }
  }

  test("Read recursive complex creature successfully") {

    val gizmoJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj("string" -> "alpha", "number" -> 85),
      "friends" -> Json.arr(),
      "social" -> "@gizmo"
    )

    val shaunJson = Json.obj(
      "name" -> "zombie",
      "isDead" -> true,
      "weight" -> 100.0F,
      "email" -> "shaun@dead.com",
      "favorites" -> Json.obj("string" -> "brain", "number" -> 2),
      "friends" -> Json.arr(gizmoJson))
    val shaunCreature = shaunJson.validate[ComplexCreature]

    shaunCreature match {
      case s: JsSuccess[ComplexCreature] => assert(true)
      case _ => fail("should be JsSuccess")
    }
  }

  test("Fail to read complex creature with 'ni' as a favorite") {

    val niJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F,
      "email" -> "ni@midnight.com",
      "favorites" -> Json.obj("string" -> "ni", "number" -> 500),
      "friends" -> Json.arr()
    )

    niJson.validate[ComplexCreature] match {
      case s: JsError => assert(true)
      case _ => fail("should be JsError")
    }
  }

  test("Write simple creature") {
    val creature = Creature("gremlins", false, 1.0F)
    val actualJson = Json.toJson(creature)

    val expectedJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F
    )

    assert(actualJson === expectedJson)
  }

  test("Write complex creature") {
    val gizmo = ComplexCreature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), Some("@gizmo"))
    val zombie = ComplexCreature("zombie", true, 100.0F, "shaun@dead.com", ("brain", 2), List(gizmo), None)
    val actualJson = Json.toJson(zombie)

    val gizmoJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj("string" -> "alpha", "number" -> 85),
      "friends" -> Json.arr(),
      "social" -> "@gizmo"
    )

    val expectedJson = Json.obj(
      "name" -> "zombie",
      "isDead" -> true,
      "weight" -> 100.0F,
      "email" -> "shaun@dead.com",
      "favorites" -> Json.obj("string" -> "brain", "number" -> 2),
      "friends" -> Json.arr(gizmoJson))

    assert(actualJson === expectedJson)
  }
}