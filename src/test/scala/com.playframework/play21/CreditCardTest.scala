package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json.{JsSuccess, JsError, Json}

class CreditCardTest extends FunSuite{

  val validMastercard = "5490123456789128"
  val invalidMastercard = "5490123456789123"
  val validVisa = "4111111111111111"
  val invalidVisa = "4111111111111114"

  test("Luhn test succeeds on valid mastercard number") {
    assert(CreditCard.luhnTest(validMastercard) === true)
  }

  test("Luhn test fails on invalid mastercard number") {
    assert(CreditCard.luhnTest(invalidMastercard) === false)
  }

  test("Luhn test succeeds on valid visa number") {
    assert(CreditCard.luhnTest(validVisa) === true)
  }

  test("Luhn test fails on invalid visa number") {
    assert(CreditCard.luhnTest(invalidVisa) === false)
  }

  test("Parse a valid visa card") {
    val createdCreditCard = CreditCard("visa", validVisa, "111" )
    val parsedCreditCard = Json.fromJson[CreditCard](
      Json.obj(
        "service" -> "visa",
        "number" -> validVisa,
        "security" -> "111"
        )
    )
    println(parsedCreditCard)

    assert(createdCreditCard === parsedCreditCard.get)
  }

  test("Parse a valid mastercard card") {
    val createdCreditCard = CreditCard("mastercard", validMastercard, "111" )
    val parsedCreditCard = Json.fromJson[CreditCard](
      Json.obj(
        "service" -> "mastercard",
        "number" -> validMastercard,
        "security" -> "111"
      )
    )
    println(parsedCreditCard)

    assert(createdCreditCard === parsedCreditCard.get)
  }

  test("Fail on invalid visa card") {
    Json.fromJson[CreditCard](
      Json.obj(
        "service" -> "visa",
        "number" -> invalidVisa,
        "security" -> "111"
      )
    ) match {
      case JsSuccess(_,_) => fail()
      case JsError(_) => println("passed")
    }
  }

  test("Fail on invalid mastercard card") {
    Json.fromJson[CreditCard](
      Json.obj(
        "service" -> "mastercard",
        "number" -> invalidMastercard,
        "security" -> "111"
      )
    ) match {
      case JsSuccess(_,_) => fail()
      case JsError(_) => println("passed")
    }
  }
}
