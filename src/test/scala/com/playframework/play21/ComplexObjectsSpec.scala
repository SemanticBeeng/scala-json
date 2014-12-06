package com.playframework.play21

import com.json.variants.Variants
import com.playframework.play21.Model4Test._
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import play.api.libs.json._

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
      val company = Company("M & N", Some(user1), Some(List(Department("tech"), Department("prod"), Department("hr"))))

      val js = Json.toJson(company)

      js.validate[Company] must equalTo(JsSuccess(company))
      // another way to do the same thing
      Json.fromJson[Company](js).get must equalTo(company)
    }
  }

  /**
   * ---------------------------
   */
  "read and write lists of complex types " in {

    val someUsers = List(AppUser(Some(1223424), "Joe Smith", AdminPermission, Department("tech")),
      AppUser(Some(1223424), "Joe Smith", AdminPermission, Department("tech")))

    val js = Json.toJson(someUsers)

    js.validate[List[AppUser]] must equalTo(JsSuccess(someUsers))
  }

  /**
   * ---------------------------
   */
  "read and write lists of primitive types " in {

    val container = ListContainer(List("1", "2"))

    val js = Json.toJson(container)

    Json.fromJson[ListContainer](js).get must equalTo(container)
  }

  /**
   * ---------------------------
   */
  "read and write lists of non primitive types " in {

    val container = PhoneList(List(PhoneNumber("415", "384858")))

    val js = Json.toJson(container)

    Json.fromJson[PhoneList](js).get must equalTo(container)
  }

  /**
   * ---------------------------
   */
  "read and write lists of non primitive types with custom list reader " in {

    val container = PhoneList2("personal", List(PhoneNumber("415", "384858")))

    val js = Json.toJson(container)

    Json.fromJson[PhoneList2](js).get must equalTo(container)
  }

  "Handle heregorenous lists" in {

    val contacts = List[Contact](EmailAddress("joe@smith.com"), PhoneNumber("415", "12345"))

    implicit val emailVariantFormat: Format[EmailAddress] = Variants.format[EmailAddress]
    implicit val phoneVariantFormat: Format[PhoneNumber] = Variants.format[PhoneNumber]

    //@todo: write custom serializer for lists containing variants
    val json: JsValue = Json.toJson(contacts)
    val print: String = Json.prettyPrint(json)
    println(print)
    success
  }

  //    /**
  //   * ---------------------------
  //   */
  //  "read and write lists of polymorphic types " in {
  //
  //    val animals = Animals("myfarm", List(Horse("Spirit"), Fish(2)))
  //
  //    val js = Json.toJson(animals)
  //
  //    println(js)
  //    success
  //    //Json.fromJson[Animals](js).get must equalTo(animals)
  //  }

}
