package com.playframework.play21

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Creature(name: String, isDead: Boolean, weight: Float)

object Creature {

  implicit val creatureReads: Reads[Creature] = (
    (__ \ "name").read[String] and
    (__ \ "isDead").read[Boolean] and
    (__ \ "weight").read[Float]
  )(Creature.apply _)

  // or using the operators inspired by Scala parser combinators for those who know them
  implicit val creatureWrites = (
    (__ \ "name").write[String] and
    (__ \ "isDead").write[Boolean] and
    (__ \ "weight").write[Float]
  )(unlift(Creature.unapply))
}

