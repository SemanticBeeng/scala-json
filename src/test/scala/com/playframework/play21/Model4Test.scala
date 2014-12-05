package com.playframework.play21

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


  //  sealed trait Contact
  //
  //  case class EmailAddress (address : String) extends Contact {
  //  }
  //
  //  case class PhoneNumber (prefix : String, number : String, extension : String) extends Contact {
  //  }
  //
  //  object Contact {
  //
  //  }

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

//  trait Animal
//
//  case class Horse(name: String) extends Animal
//
//  case class Fish(weight: Double) extends Animal
//
//  case class Animals(animals: List[Horse])
//
//  object Horse {
//    implicit val format = Json.format[Horse]
//  }
//
//  object Fish {
//    implicit val format = Json.format[Fish]
//  }
//
//  object Animals {
//
//    implicit val animalsReads: Reads[Animals] = (
//      (__ \ "animals").read(Reads.list[Horse](animalReads))
//      )(Animals.apply _)
//
//    implicit val animalsWrites: Writes[Animals] = (
//      (__ \ "animals").write(Writes.list[Horse](animalWrites))
//      )(unlift(Animals.unapply))
//
//
//  }
//
//  implicit object animalReads extends Reads[Horse] {
//
//    override def reads(j: JsValue): JsResult[Horse] = ???
////    j match {
////      case h: Horse => Json.reads[Horse].reads(h)
////      case f: Fish => Json.reads[Fish].reads(f)
////    }
//  }
//
//  implicit object animalWrites extends Writes[Horse] {
//    override def writes(o: Horse): JsValue = o match {
//      case h: Horse => Json.writes[Horse].writes(h)
//      case f: Fish => Json.writes[Fish].writes(f)
//    }
//  }


}