package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json._

class WritesTestSuite extends FunSuite {

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

    val gizmo = ComplexCreature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), List(), Some("@gizmo"))
    val zombie = ComplexCreature("zombie", true, 100.0F, "shaun@dead.com", ("brain", 2), List(gizmo), List(), None)
//    val gizmo = ComplexCreature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85),
//      List(), List(),/*List(PhoneNumber("415", "123456"), PhoneNumber("415", "9393994")), */Some("@gizmo"))
//
//    val zombie = ComplexCreature("zombie", true, 100.0F, "shaun@dead.com", ("brain", 2),
//      List(gizmo), List(), None)

    val actualJson = Json.toJson(zombie)

    val gizmoJson = Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0F,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj("string" -> "alpha", "number" -> 85),
      "friends" -> Json.arr(),
      "phoneNumbers" -> Json.arr(),
      "social" -> "@gizmo"
    )

    val expectedJson = Json.obj(
      "name" -> "zombie",
      "isDead" -> true,
      "weight" -> 100.0F,
      "email" -> "shaun@dead.com",
      "favorites" -> Json.obj("string" -> "brain", "number" -> 2),
      "friends" -> Json.arr(gizmoJson),
      "phoneNumbers" -> Json.arr())

    assert(actualJson === expectedJson)
  }
//  {
//
//    {
//      "name": "zombie", "isDead": true, "weight": 100.0, "email": "shaun@dead.com", "favorites": {"string": "brain", "number": 2}, "friends":[
//      {
//        "name": "gremlins", "isDead": false, "weight": 1.0, "email": "gizmo@midnight.com", "favorites": {"string": "alpha", "number": 85}, "friends":[], "phoneNumbers":[
//        {
//          "prefix": "415", "number": "123456"
//        }], "social": "@gizmo"
//      }], "phoneNumbers":[]
//    }
//
//    {
//      "name": "zombie", "isDead": true, "weight": 100.0, "email": "shaun@dead.com", "favorites": {"string": "brain", "number": 2}, "friends":[
//      {
//        "name": "gremlins", "isDead": false, "weight": 1.0, "email": "gizmo@midnight.com", "favorites": {"string": "alpha", "number": 85}, "friends":[], "social": "@gizmo"
//      }]
//    }

//  }
}