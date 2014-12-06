package com.playframework.play21

import com.json.variants.Variants
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 *
 */
object Model4Test {

  /**
   * ---------------------
   */
  sealed trait Permission

  case object AdminPermission extends Permission

  case object UserPermission extends Permission

  object Permission {

    implicit val permFormat = Json.format[Permission]

    def apply(num: Int): Permission = num match {
      case 0 => AdminPermission
      case _ => UserPermission
    }

    def unapply(p: Permission): Option[Int] = p match {
      case AdminPermission => Some(0)
      case UserPermission => Some(1)
    }
  }

  sealed trait Contact

  sealed case class PhoneNumber(prefix: String, number: String) extends Contact

  object PhoneNumber {
    implicit val format = Json.format[PhoneNumber]
    implicit val variantFormat: Format[PhoneNumber] = Variants.format[PhoneNumber]
  }

  sealed case class EmailAddress(address: String) extends Contact

  object EmailAddress {

    implicit val format = Json.format[EmailAddress]
    implicit val variantFormat: Format[EmailAddress] = Variants.format[EmailAddress]
  }

  object Department {
    implicit val depFormat = Json.format[Department]

  }

  /**
   * Must be defined before Company's format
   */
  object AppUser {

    implicit val userFormat = Json.format[AppUser]

  }

  object Company {
    implicit val companyFormat = Json.format[Company]

  }

  object Inventory {
    implicit val invFormat = Json.format[Inventory]
  }


  /**
   * ---------------------
   */
  case class Department(name: String)


  /**
   * ---------------------
   */
  case class Company(name: String, admin: Option[AppUser] = None, departments: Option[List[Department]])


  /**
   * ---------------------
   */
  case class Inventory(name: String, inventoryType: String, department: Department)

  /**
   * ---------------------
   */
  case class AppUser(
                      id: Option[Long] = None,
                      name: String,
                      permission: Permission = UserPermission,
                      department: Department
                      )


  case class ListContainer(items: List[String])

  object ListContainer {
    implicit val format = Json.format[ListContainer]
  }

  case class PhoneList(
                        phoneNumbers: List[PhoneNumber]
                        )

  object PhoneList {
    implicit val format = Json.format[PhoneList]
  }

  case class PhoneList2(
                         name: String,
                         phoneNumbers: List[PhoneNumber]
                         )

  object PhoneList2 {

    implicit val pleads: Reads[PhoneList2] = (
      (__ \ "name").read[String] and
        (__ \ "phoneNumbers").read(Reads.list[PhoneNumber](Json.reads[PhoneNumber]))
      )(PhoneList2.apply _)

    implicit val plWrites: Writes[PhoneList2] = (
      (__ \ "name").write[String] and
        (__ \ "phoneNumbers").write(Writes.list[PhoneNumber](Json.writes[PhoneNumber]))
      )(unlift(PhoneList2.unapply))

  }

  trait Animal

  case class Horse(name: String) extends Animal

  case class Fish(weight: Double) extends Animal

  case class Animals(listName: String, animals: List[Animal])

  object Horse {
    implicit val format = Json.format[Horse]
  }

  object Fish {
    implicit val format = Json.format[Fish]
  }

//  object Animals {
//
//    implicit val animalsReads: Reads[Animals] = (
//      (__ \ "listName").read[String] and
//        (__ \ "animals").read(Reads.list[Animal](animalReads))
//      )(Animals.apply _)
//
//    implicit val animalsWrites: Writes[Animals] = (
//      (__ \ "listName").write[String] and
//        (__ \ "animals").write(Writes.list[Animal](animalWrites))
//      )(unlift(Animals.unapply))
//  }
//
//  implicit object animalReads extends Reads[Animal] {
//
//    override def reads(j: JsValue): JsResult[Animal] = (
//      (__ \ "horse").read[String].map[Animal](Horse) |
//        (__ \ "fish").read[Int].map[Animal](Fish)
//      )
//  }
//
//  implicit object animalWrites extends Writes[Animal] {
//
//    override def writes(o: Animal): JsValue = o match {
//      case h: Horse => (__ \ "horse").write[Horse].writes(h)
//      case f: Fish => (__ \ "fish").write[Fish].writes(f)
//    }
//  }


}
