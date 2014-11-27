package com.playframework.play21

import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
//import play.api.libs.json.Reads._
import play.api.libs.json._

case class CreditCard(service: String, number: String, security: String)

object CreditCard {

  def luhnTest(number: String): Boolean = {
    val digits = number.reverse.map {
      _.toString.toInt
    }
    val s = digits.grouped(2) map {
      t => t(0) +
        (if (t.length > 1) (t(1) * 2) % 10 + t(1) / 5 else 0)
    }
    s.sum % 10 == 0
  }

  def creditCardNumberReads(implicit r: Reads[String]): Reads[String] =
    Reads.filter(ValidationError("validate.error.luhn-test"))(luhnTest(_))

  implicit val securityCodeLengthReads = new Reads[String] {

    def lengthOfSecurityCode(number: String): Int = {
      number match {
        // American Express
        case n if n.startsWith("34") => 4
        case n if n.startsWith("37") => 4

        // All other issuers
        case _ => 3
      }
    }

    def reads(js: JsValue): JsResult[String] = {
      val number = (js \ "number").as[String].trim
      val cvv = (js \ "security").as[String].trim

      val expectedLength = lengthOfSecurityCode(number)
      val actualLength = cvv.length

      if (expectedLength == actualLength) {
        JsSuccess(cvv)
      } else {
        JsError(s"Invalid security code length")
      }
    }
  }

  // to avoid compile error
  import play.api.libs.json.Reads.minLength

  implicit val paymentReads: Reads[CreditCard] = (
    (__ \ 'service).read[String](minLength[String](4)) and
      (__ \ 'number).read[String](creditCardNumberReads) and
      __.read(securityCodeLengthReads)
    )(CreditCard.apply _)

}
