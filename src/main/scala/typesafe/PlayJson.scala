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

object PlayJson {
  def main(args: Array[String]) {
    val kaylee = Koala("kaylee", EucalyptusTree(10, 23))

    println(Json.prettyPrint(Json.toJson(kaylee)))

    Json.fromJson[Koala](
      Json.obj(
        "name" -> "kaylee",
        "home" -> Json.obj(
          "col" -> 10,
          "row" -> 23
        )
      )
    )
  }
}
