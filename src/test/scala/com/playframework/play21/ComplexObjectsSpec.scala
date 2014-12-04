package com.playframework.play21

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import play.api.libs.json._
import com.playframework.play21.Model4Test._

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class ComplexObjectsSpec extends Specification {


  /**
   * -----------------------------
   */
  "JSON reads" should {


    /**
     * ---------------------------
     */
    "read and write complex types" in {

      val user1 = AppUser(Some(1223424), "Joe Smith", AdminPermission, Department("tech"))

      val js = Json.toJson(user1)

      js.validate[AppUser] must equalTo(JsSuccess(user1))
    }

    /**
     * ---------------------------
     */
    "read and write complex types with lists" in {

      val user1 = AppUser(Some(1223424), "Joe Smith", AdminPermission, Department("tech"))
      val company = Company("M & N", Some(user1), List(Department("tech"), Department("prod"), Department("hr")))

      val js = Json.toJson(company)

      js.validate[Company] must equalTo(JsSuccess(company))
    }
  }
}
