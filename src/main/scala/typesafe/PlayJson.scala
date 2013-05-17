package typesafe

import play.api.libs.json._

case class EucalyptusTree(col:Int, row: Int)

object EucalyptusTree{
  implicit val fmt = Json.format[EucalyptusTree]
}

case class Koala(name: String, home: EucalyptusTree)

object Koala{
  implicit val fmt = Json.format[Koala]
}
