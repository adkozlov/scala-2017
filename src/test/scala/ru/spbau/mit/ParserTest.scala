package ru.spbau.mit

import org.scalatest._
import org.scalatest.prop._

class ParserTest extends PropSpec with Matchers with TableDrivenPropertyChecks {
  val parser = new Parser()

  val correctExprElements =
    Table(
      ("stringExpr", "elements"),
      ("1+1", List(1.0, new Add(), 1.0)),
      ("(1 + 1)", List(new OpenBracket(), 1.0, new Add(), 1.0, new CloseBracket())),
      ("1 * (4.5 + 10.12)", List(1.0, new Product(), new OpenBracket(), 4.5, new Add(), 10.12, new CloseBracket())),
      ("sin( 1 + 4.5 )", List(new Sin(), new OpenBracket(), 1.0, new Add(), 4.5, new CloseBracket())),
      ("5.0 / 4 + 1", List(5.0, new Divide(), 4.0, new Add(), 1.0))
    )

  property("Correct expressions should parse right.") {
    forAll(correctExprElements) { (stringExpr, elements) =>
      assert(parser.parseLexems(stringExpr) == elements)
    }
  }

  val incorrectExprElements =
    Table(
      "stringExpr",
      "pa",
      "1.1.",
    )

    property("Incorect expression should throw an exception.") {
      forAll(incorrectExprElements) { (stringExpr) =>
        val parser = new Parser()
        a [IllegalArgumentException] should be thrownBy parser.parseLexems(stringExpr)
      }
    }

}
