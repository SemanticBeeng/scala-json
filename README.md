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

### JSON Reads ###

Mapping a Json structure to a case class

	// if you need Json structures in your scope
	import play.api.libs.json._
	// IMPORTANT import this to have the required tools in your scope
	import play.api.libs.functional.syntax._

	case class Creature(name: String, isDead: Boolean, weight: Float)

	object Creature {
	implicit val creatureReads: Reads[Creature] = (
	(__ \ "name").read[String] and
	  (__ \ "isDead").read[Boolean] and
	  (__ \ "weight").read[Float]
	)(Creature.apply _)

Let's break this down

- `(__ \ "name")` is the JsPath where you gonna apply `read[String]`
- `and` is just an operator meaning `Reads[A] and Reads[B] => Builder[Reads[A ~ B]]` where `~` is inspired by Scala parser combinators
	- `A ~ B` just means `Combine A and B` but it doesn't suppose the way it is combined (can be a tuple, an object, whatever, ...)
	- `Builder` is not a real type but I introduce it just to tell that the operator `and` doesn't create directly a `Reads[A ~ B]` but an intermediate structure that is able to build a `Reads[A ~ B]` or to combine with another `Reads[C]`
- `(Creature.apply _)` build a `Reads[Creature]`

#### Reads for complex structures ####

Let's imagine a complex creature:

- it's relatively modern creature that has an email address but hates email addresses with less than 5 characters
- may have 2 favorites data:
	- 1 String (called `string` in JSON) which shall not be `ni` (because it loves Monty Python) and then to skip the first 2 chars
	- 1 Int (called `number` in JSON) which can be less than `86` or more than `875`
- may have friend creatures
- may have an social account

So something like this:

	case class Creature(
	  name: String,
	  isDead: Boolean,
	  weight: Float,
	  email: String, // email format and minLength(5)
	  favorites: (String, Int), // the stupid favorites
	  friends: List[Creature] = Nil, // yes by default it has no friend
	  social: Option[String] = None // by default, it's not social
	)

The reads function can be realized like this

	implicit val creatureReads: Reads[Creature] = (
	  (__ \ "name").read[String] and
	  (__ \ "isDead").read[Boolean] and
	  (__ \ "weight").read[Float] and
	  (__ \ "email").read(email keepAnd minLength[String](5)) and
	  (__ \ "favorites").read(
	      (__ \ "string").read[String]( notEqualReads("ni") andKeep skipReads ) and
	      (__ \ "number").read[Int]( max(86) or min(875) )
	      tupled
	  ) and
	  (__ \ "friends").lazyRead( list[Creature](creatureReads) ) and
	  (__ \ "social").readOpt[String]
	)(Creature)

Let's break this down

- `(__ \ "email").read(email keepAnd minLength[String](5))`
	- `(__ \ "email").read(...)` gets the `JsPath`
	- `email keepAnd minLength[String](5) => Reads[String]` is a `Js` validator that verifies JsValue:
		1. is a String : `email: Reads[String]` so no need to specify type here
		2. has email format
		3. has min length of 5
	- The `keepAnd` operator (aka`<~`) validates both sides but if succeeded, it keeps only the result on left side.
- `notEqualReads("ni") andKeep skipReads`
	- No need to write `notEqualReads[String]("ni")` because `String` type is inferred
	- `skipReads` is a customReads that skips the first 2 chars
	- `andKeep` operator (aka `~>`) validates the left and right side and if both succeed, only keeps the result on right side.
- `max(86) or min(875)`
	- the classic `OR` logic operator
- `tupled`
	 - `tuplizes` your Builder: `Builder[Reads[(A, B)]].tupled => Reads[(A, B)]`
- `(__ \ "friend").lazyRead( list[Creature](creatureReads) )`
	- `lazyRead` expects a `Reads[A]` value _passed by name_ to allow the type recursive construction
- `(__ \ "social").readOpt[String]`
	- read an `Option`

#### Validating a `JSValue` ####

There are three mechanisms to read a Json structure. The first one validates a `JsValue`

1. `validate` returns a `JsResult`, which is either a `JsSuccess` or a `JsError` (which in turn is a wrapper for **all** validation errors)

So now the preferred method to deal with a `JsValue` is to use `fold`:

	val res: JsResult[Creature] = js.validate[Creature]

	// managing the success/error and potentially return something
	res.fold(
	  valid = { c => println( c ); c.name },
	  invalid = { e => println( e ); e }
	)

So in the case of a Play action you would do something like this

	// a classic Play action
	def getNameOnly = Action(parse.json) { request =>
	  val json = request.body
	  json.validate[Creature].fold(
	    valid = ( res => Ok(res.name) ),
	    invalid = ( e => BadRequest(e.toString) )
	  )
	}

#### Under the hood ####

There are now three mechanisms to read a Json structure

	trait JsValue {
	  def validate[T](implicit _reads: Reads[T]): JsResult[T] = _reads.reads(this)

	  // same behavior but it throws a specific RuntimeException JsResultException now
	  def as[T](implicit fjs: Reads[T]): T

	  // exactly the same behavior has before
	  def asOpt[T](implicit fjs: Reads[T]): Option[T]
	}

Remapping the type, wrapping it in a `JsResult`

	trait Reads[A] {
	  self : A =>
	  // convert the JsValue into a A
	  def reads(json: JsValue): JsResult[A]
	}

where `JsResult` can be of two types:

- `JsSuccess[A]` when `reads` succeeds
- `JsError[A]` when `reads` fails

To create a JsError, there are a few helpers

	val errors1 = JsError( __ \ 'isDead, ValidationError("validate.error.missing", "isDead") )
	val errors2 = JsError( __ \ 'name, ValidationError("validate.error.missing", "name") )

An advantage of `JsError` is that it's a cumulative error which can store several errors discovered in the Json at different `JsPath`s

	scala> val errors = errors1 ++ errors2
	errors: JsError(List((/isDead,List(ValidationError(validate.error.missing,WrappedArray(isDead)))), (/name,List(ValidationError(validate.error.missing,WrappedArray(name))))))

## Future ##

- [JsZipper : Play2 Json Advanced (& Monadic) Manipulations](http://mandubian.com/2013/05/01/JsZipper/)
- [An Introduction To Scala Parser Combinators - Part 1: Parser Basics](http://henkelmann.eu/2011/01/13/an_introduction_to_scala_parser_combinators)
- [An Introduction To Scala Parser Combinators - Part 2: Parsing Literal Expressions](http://henkelmann.eu/2011/01/28/an_introduction_to_scala_parser_combinators-part_2_literal_expressions)
- [An Introduction To Scala Parser Combinators - Part 3: Writing unit tests for parsers](http://henkelmann.eu/2011/01/29/an_introduction_to_scala_parser_combinators-part_3_unit_tests)

## Sources ##

- [The Play JSON library Basics](http://www.playframework.com/documentation/2.1.1/ScalaJson)
- [Unveiling Play 2.1 Json API - Part 1 : JsPath & Reads Combinators](http://mandubian.com/2012/09/08/unveiling-play-2-dot-1-json-api-part1-jspath-reads-combinators/)
- [Unveiling Play 2.1 Json API - Part 2 : Writes/Format Combinators](http://mandubian.com/2012/10/01/unveiling-play-2-dot-1-json-api-part2-writes-format-combinators/)
- [Unveiling Play 2.1 Json API - Part 3 : JSON Transformers](http://mandubian.com/2012/10/29/unveiling-play-2-dot-1-json-api-part3-json-transformers/)
- [Unveiling Play 2.1 Json API - Bonus : JSON Inception (Based on Scala 2.10 Macros)](http://mandubian.com/2012/11/11/JSON-inception/)