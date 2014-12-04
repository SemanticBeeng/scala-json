package com.playframework.play21

;

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import play.api.libs.json._

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class ComplexObjectsSpec extends Specification {

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

  case class Department(name: String)

  object Department {
    implicit val depFormat = Json.format[Department]

  }

  case class Inventory(name: String, inventoryType: String, department: Department)

  object Inventory {
    implicit val invFormat = Json.format[Inventory]
  }

  case class AppUser(
                      id: Option[Long] = None,
                      name: String,
                      permission: Permission = UserPermission,
                      department: Department
                      )

  object AppUser {

    implicit val userFormat = Json.format[AppUser]

  }


  /**
   * -----------------------------
   */
  "JSON reads" should {


    /**
     * ---------------------------
     */
    "write complex types" in {

      val user1 = AppUser(Some(1223424), "Joe Smith", AdminPermission, Department("tech"))

      val js = Json.toJson(user1)

      js.validate[AppUser] must equalTo(JsSuccess(user1))

      //      {
      //        val js = Json.toJson(AdminPermission)
      //
      //        js.validate[Permission] must equalTo(JsSuccess(AdminPermission))
      //      }

      //      {
      //      }
    }
  }
}
