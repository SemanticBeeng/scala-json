package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json._
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.data.validation.ValidationError

class NavigationSuite extends FunSuite {

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

}