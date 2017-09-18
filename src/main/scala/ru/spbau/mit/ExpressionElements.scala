package ru.spbau.mit

import scala.collection.immutable.HashMap

object ExpressionElements {
  val keyWords = Array("\\+", "-", "\\*", "/")
  val keyWordsWithArgs = Array("sin", "cos")
  val constants = Array("one")
  val openBracket = "("
  val closeBracket = ")"

  val classes: Map[String, Operation] = HashMap[String, Operation](
    ("+", new Add()),
    ("-", new Subtract()),
    ("*", new Product()),
    ("/", new Divide()),
    ("sin", new Sin()),
    ("one", new One()),
    ("cos", new Cos())
  )
}
