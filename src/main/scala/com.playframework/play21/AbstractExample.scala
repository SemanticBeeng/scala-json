package com.playframework.play21

import play.api.libs.json._

object AbstractExample {

  val json = Json.obj(
    "key1" -> "value1",
    "key2" -> Json.obj(
      "key21" -> 123,
      "key22" -> true,
      "key23" -> Json.arr("alpha", "beta", "gamma"),
      "key24" -> Json.obj(
        "key241" -> 234.123,
        "key242" -> "value242"
      )
    ),
    "key3" -> 234
  )

}