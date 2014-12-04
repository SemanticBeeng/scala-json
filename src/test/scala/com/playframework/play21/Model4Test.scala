package com.playframework.play21

import play.api.libs.json.Json

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

  /**
   * ---------------------
   */
  case class Department(name: String)


  /**
   * ---------------------
   */
  case class Company(name: String, admin: Option[AppUser] = None, departments: List[Department])


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

}
