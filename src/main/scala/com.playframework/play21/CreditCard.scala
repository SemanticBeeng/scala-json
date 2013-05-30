package com.playframework.play21

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError

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

  def creditCardNumberReads(implicit r: Reads[String]): Reads[String] = Reads.filter(ValidationError("validate.error.luhn-test"))(luhnTest(_))

  implicit val paymentReads: Reads[CreditCard] = (
    (__ \ 'service).read[String](minLength[String](4)) and
      (__ \ 'number).read[String](creditCardNumberReads) and
      (__ \ 'security).read[String](minLength[String](3))
    )(CreditCard.apply _)

}
