package com.playframework.play21

import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class ComplexCreature(
  name: String,
  isDead: Boolean,
  weight: Float,
  email: String, // email format and minLength(5)
  favorites: (String, Int), // the stupid favorites
  friends: List[ComplexCreature] = Nil, // yes by default it has no friend
  social: Option[String] = None // by default, it's not social
  )

object ComplexCreature {

  // a reads that verifies your value is not equal to a given value
  def notEqualReads[T](v: T)(implicit r: Reads[T]): Reads[T] = Reads.filterNot(ValidationError("validate.error.unexpected.value", v))(_ == v)

  def skipFirstTwoCharsReads(implicit r: Reads[String]): Reads[String] = r.map(_.substring(2))

  implicit val complexCreatureReads: Reads[ComplexCreature] = (
    (__ \ "name").read[String] and
    (__ \ "isDead").read[Boolean] and
    (__ \ "weight").read[Float] and
    (__ \ "email").read(email keepAnd minLength[String](5)) and
    (__ \ "favorites").read(
      (__ \ "string").read[String](notEqualReads("ni") andKeep skipFirstTwoCharsReads) and
        (__ \ "number").read[Int](max(86) or min(875)) tupled
    ) and
      (__ \ "friends").lazyRead(list[ComplexCreature](complexCreatureReads)) and
      (__ \ "social").readNullable[String] //@todo readOpt??
  )(ComplexCreature.apply _)

  implicit val complexCreatureWrites: Writes[ComplexCreature] = (
    (__ \ "name").write[String] and
    (__ \ "isDead").write[Boolean] and
    (__ \ "weight").write[Float] and
    (__ \ "email").write[String] and
    (__ \ "favorites").write(
      (__ \ "string").write[String] and
        (__ \ "number").write[Int] tupled
    ) and
      (__ \ "friends").lazyWrite(Writes.traversableWrites[ComplexCreature](complexCreatureWrites)) and
      (__ \ "social").writeNullable[String] //@todo writeOpt??
  )(unlift(ComplexCreature.unapply))

}