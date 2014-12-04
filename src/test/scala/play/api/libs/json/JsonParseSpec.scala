/*
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api.libs.json

import org.specs2.mutable._

object JsonParseSpec extends Specification {

  "JSON" should {
    "parse json strings" in {

      val json: JsValue = Json.parse( """
          {
            "user": {
              "name" : "toto",
              "age" : 25,
              "email" : "toto@jmail.com",
              "isAlive" : true,
              "friend" : {
                "name" : "tata",
                "age" : 20,
                "email" : "tata@coldmail.com"
              }
            }
          }
                                      """)

      println(json)
      success
    }
  }

}

