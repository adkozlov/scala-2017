package ru.spbau.mit

import org.scalatest._
import org.scalatest.prop._

class CalculatorTest extends PropSpec with Matchers with TableDrivenPropertyChecks {
  val calculator = new Calculator()

  val correctExpr = Table(
    ("stringExpr", "result"),
    ("1+1", 2),
    ("(1 + 1)", 2),
    ("1 * (4.5 + 10.12)", 14.62),
    ("sin( 1 - 1 )", 0),
    ("5.0 / 2 + 1", 3.5),
    ("one + 5", 6),
    ("cos(0)", 1)
  )

  property("Correct expressions must be calculated and given the right answer.") {
    forAll(correctExpr) { (stringExpr, result) =>
      assert(calculator.calculateResult(stringExpr) == result)
    }
  }

  val incorrectExpr = Table(
    "stringExpr",
    "1+",
    "+",
    "(1+1",
    "1+-1",
    "1*/2",
    "((1+1)",
    "(1+1))",
    "sin()"
  )

  property("Incorrect expressions must be thrown a CalculatorException.") {
    forAll(incorrectExpr) { (stringExpr) =>
      val calculator = new Calculator()
      a [CalculatorException] should be thrownBy calculator.calculateResult(stringExpr)
    }
  }
}
