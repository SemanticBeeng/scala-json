package com.playframework.play21

import org.scalatest.FunSuite
import play.api.libs.json.{JsSuccess, JsError, Json}

class CreditCardTest extends FunSuite{

  val Visa = "visa"
  val validVisa = "4111111111111111"
  val invalidVisa = "4111111111111114"

  val Mastercard = "mastercard"
  val validMastercard = "5490123456789128"
  val invalidMastercard = "5490123456789123"

  val AmericanEXpress = "american-express"
  val validAmericanExpress = "378161408561557"
  val invalidAmericanExpress = "378161408561558"

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

  test("Luhn test succeeds on valid American Express number") {
    assert(CreditCard.luhnTest(validAmericanExpress) === true)
  }

  test("Luhn test fails on invalid American Express number") {
    assert(CreditCard.luhnTest(invalidAmericanExpress) === false)
  }

  test("Parse a valid visa card") {
    val createdCreditCard = CreditCard(Visa, validVisa, "111" )
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

  test("Parse a valid American Express card with four digit pin") {
    val createdCreditCard = CreditCard(AmericanEXpress, validAmericanExpress, "1111" )
    val parsedCreditCard = Json.fromJson[CreditCard](
      Json.obj(
        "service" -> AmericanEXpress,
        "number" -> validAmericanExpress,
        "security" -> "1111"
      )
    )
    println(parsedCreditCard)

    assert(createdCreditCard === parsedCreditCard.get)
  }

  test("Fail on valid American Express card with three digit pin") {
    Json.fromJson[CreditCard](
      Json.obj(
        "service" -> AmericanEXpress,
        "number" -> validAmericanExpress,
        "security" -> "111"
      )
    ) match {
      case JsSuccess(_,_) => fail()
      case JsError(_) => println("passed")
    }
  }

  test("Parse a valid Mastercard card") {
    val createdCreditCard = CreditCard(Mastercard, validMastercard, "111" )
    val parsedCreditCard = Json.fromJson[CreditCard](
      Json.obj(
        "service" -> Mastercard,
        "number" -> validMastercard,
        "security" -> "111"
      )
    )
    println(parsedCreditCard)

    assert(createdCreditCard === parsedCreditCard.get)
  }

  test("Fail on invalid Visa card") {
    Json.fromJson[CreditCard](
      Json.obj(
        "service" -> Visa,
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
        "service" -> Mastercard,
        "number" -> invalidMastercard,
        "security" -> "111"
      )
    ) match {
      case JsSuccess(_,_) => fail()
      case JsError(_) => println("passed")
    }
  }


}
