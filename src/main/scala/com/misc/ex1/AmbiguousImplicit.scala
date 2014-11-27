package com.misc.ex1

/**
 * Resolving ambiguity
 * http://stackoverflow.com/questions/25431878/ambiguous-implicit-values
 */
class AmbiguousImplicit {


  // In my application I have several domain classes
  case class Foo(baz: String)
  case class Bar(baz: String)

  // And a class that is able to construct domain object from a string
  class Reads[A] {
    def read(s: String): A = throw new Exception("not implemented")
  }

  // Next, there are implicit deserializers
  implicit val fooReads = new Reads[Foo]
  implicit val barReads = new Reads[Bar]

  // And a helper to convert strings to one of domain classes
  def convert[A](s: String)(implicit reads: Reads[A]): A = reads.read(s)

  // Unfortunately, when trying to use it
  // def f(s: String): Foo = convert[Foo](s)
  // I get compiler errors like
  //
  // error: ambiguous implicit values:
  // both value fooReads of type => Reads[Foo]
  // and value barReads of type => Reads[Bar]
  // match expected type Reads[A]
  // def f(s: String): Foo = convert(s)

  // Solution
  def f(s: String): Foo = convert[Foo](s)

}
