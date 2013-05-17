# Scala JSON #

The goal of this project is to try out ways to work with JSON data using Scala.

	git clone git@github.com:oschrenk/scala-json.git
	cd scala-json

If you use Eclipse

	make eclipse

If you use IntelliJ IDEA

	make idea

## JSON is an AST (Abstract Syntax Tree) ##

JSON data can be seen as a tree structure using the 2 following structures:

- `JSON object` containing a set of `name` / `value` pairs, where
	- `name` is a String and the
	- `value` can be a
		- string
		- number
		- another JSON object
		- JSON array
		- true/false
		- null
- `JSON array` is a sequence of values from the types listed above

## play-json ##

The [Play Framework](http://www.playframework.com/) introduced a new JSON API alongside the release of version 2.1. It is also available as a standalone release right now, and will be delivered as such starting with Play 2.2.

For parsing it uses the Java based JSON library [Jackson](http://jackson.codehaus.org/)

### Creating JSON ###

`play-json` offers a straight forward API for creating JSON directly inside your code.

	val js = Json.obj(
	  "key1" -> "value1",
	  "key2" -> 234,
	  "key3" -> Json.obj(
	    "key31" -> true,
	    "key32" -> Json.arr("alpha", "beta", 234.13),
	    "key33" -> Json.obj("key1" -> "value2", "key34" -> "value34")
	  )
	)

### Navigating JSON using JsPath ###

You navigate JSON structures using the `JsPath` object, or preferably using its shorthand alias `__` (two underscores).

There are two operators

- `\` which returns the value of a specific key on the next level as a `List`
- `\` which traverses the tree and returns the values for all instances of a specific key as a `List`

Given the following `JsValue` as an example structure

	 val json = Json.obj(
      "key1" -> "value1",
      "key2" -> 234,
      "key3" -> Json.obj(
        "key31" -> true,
        "key32" -> Json.arr("alpha", "beta", 234.13),
        "key33" -> Json.obj("key1" -> "value2", "key34" -> "value34")
      )
    )

You can easily drill down the JSON structure:

- `(__ \ "key1")(json)` results in `List(JsString("value1")))`
- `(__ \ "key3" \ "key33")(json)` results `List(Json.obj("key1" -> "value2", "key34" -> "value34")))`

You can specify the index of an array, so that

- `(__ \ "key3" \ "key32")(2)(json)` results in `List(JsNumber(234.13)))`

If you are are interested in all instances of a key:

- `(__ \\ "key1")(json)` results in `List(JsString("value1"), JsString("value2")))`

## Future ##

- [JsZipper : Play2 Json Advanced (& Monadic) Manipulations](http://mandubian.com/2013/05/01/JsZipper/)

## Sources ##

- [The Play JSON library Basics](http://www.playframework.com/documentation/2.1.1/ScalaJson)
- [Unveiling Play 2.1 Json API - Part 1 : JsPath & Reads Combinators](http://mandubian.com/2012/09/08/unveiling-play-2-dot-1-json-api-part1-jspath-reads-combinators/)
- [Unveiling Play 2.1 Json API - Part 2 : Writes/Format Combinators](http://mandubian.com/2012/10/01/unveiling-play-2-dot-1-json-api-part2-writes-format-combinators/)
- [Unveiling Play 2.1 Json API - Part 3 : JSON Transformers](http://mandubian.com/2012/10/29/unveiling-play-2-dot-1-json-api-part3-json-transformers/)
- [Unveiling Play 2.1 Json API - Bonus : JSON Inception (Based on Scala 2.10 Macros)](http://mandubian.com/2012/11/11/JSON-inception/)
