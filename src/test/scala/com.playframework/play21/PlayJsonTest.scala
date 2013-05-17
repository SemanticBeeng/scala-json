package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json.Json

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
}