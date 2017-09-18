package ru.spbau.mit

import scala.collection.mutable.ListBuffer

object Main {
  def executeExpr(expr: String): Unit = {
    val calculator = new Calculator()
    val res = calculator.calculateResult(expr)
    println(res)
  }

  def main(args: Array[String]): Unit = {
    val expr = new ListBuffer[String]
    expr += "1+1"
    expr += "6*(1 + 1)"
    expr += "6 + (-5)"
    expr += "sin(0) + 4"
    for (curExpr <- expr) {
      executeExpr(curExpr)
    }
  }
}