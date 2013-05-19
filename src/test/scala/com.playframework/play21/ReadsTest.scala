package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json._
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.data.validation.ValidationError

class ReadsTestSuite extends FunSuite {

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
}