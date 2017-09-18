package ru.spbau.mit

import scala.collection.mutable

class Calculator() {
  private var opStack = new mutable.ListBuffer[Operation]()
  private var valStack = new mutable.ListBuffer[Double]()

  def calculateResult(expr: String): Double = {
    val parser = new Parser()
    val listLexems = parser.parseLexems(expr)

    var prevLexem: Any = None

    for (lexem <- listLexems) {
      // validation lexem
      lexem match {
        case number @ (_:Double | _:Constant) =>
          if (prevLexem.isInstanceOf[Double] ||
            prevLexem.isInstanceOf[CloseBracket] ||
            prevLexem.isInstanceOf[Constant]
          )
            throw new CalculatorException("Syntax error on the lexem $lexem")

        case operation: CloseBracket =>
          if (prevLexem.isInstanceOf[UnaryOperation] ||
            prevLexem.isInstanceOf[BinaryOperation] ||
            prevLexem == None
          )
            throw new CalculatorException("Syntax error on the lexem $lexem.name")

        case operation: OpenBracket =>
          if (prevLexem.isInstanceOf[Constant] ||
            prevLexem.isInstanceOf[Double]
          )
            throw new CalculatorException("Syntax error on the lexem $lexem.name")

        case operation: BinaryOperation =>
          if (prevLexem.isInstanceOf[OpenBracket] ||
            prevLexem.isInstanceOf[UnaryOperation] ||
            prevLexem.isInstanceOf[BinaryOperation] ||
            prevLexem == None
          )
            throw new CalculatorException("Syntax error on the lexem $lexem.name")

        case operation: UnaryOperation =>
          if (prevLexem.isInstanceOf[Constant] ||
            prevLexem.isInstanceOf[Double] ||
            prevLexem.isInstanceOf[CloseBracket]
          )
            throw new CalculatorException("Syntax error on the lexem $lexem.name")
      }

      // actions
      lexem match {
        case number: Double =>
          valStack += number

        case operation: CloseBracket =>
          calculateWhile((op: Operation) => !op.isInstanceOf[OpenBracket])
          if (opStack.isEmpty)
            throw new CalculatorException("Wrong parenthesis")
          opStack.remove(opStack.length - 1)

        case operation: Operation =>
          if (
              opStack.nonEmpty &&
              !opStack.last.isInstanceOf[OpenBracket] &&
              opStack.last.getPriority > operation.getPriority
          ) {
            calculateWhile((op: Operation) => op.getPriority >= operation.getPriority)
          }
          opStack += operation
      }

      prevLexem = lexem
    }

    if (
      prevLexem.isInstanceOf[OpenBracket] ||
      prevLexem.isInstanceOf[UnaryOperation] ||
      prevLexem.isInstanceOf[BinaryOperation]
    )
      throw new CalculatorException("Expression isn't finished")

    calculateWhile((op: Operation) => true)

    if (opStack.nonEmpty)
      throw new CalculatorException("Expression isn't finished")

    val result = valStack.last
    valStack.remove(valStack.length - 1)
    result
  }

  private def calculateWhile(function: Operation => Boolean): Unit = {
    while (opStack.nonEmpty && function(opStack.last)) {
      try {
        opStack.last match {
          case unary: UnaryOperation =>
            val res = unary.eval(valStack.last)
            valStack.remove(valStack.length - 1)
            valStack += res
          case binary: BinaryOperation =>
            val op2 = valStack.last
            valStack.remove(valStack.length - 1)
            val op1 = valStack.last
            valStack.remove(valStack.length - 1)
            valStack += binary.eval(op1, op2)
          case const: Constant =>
            valStack += const.eval()
          case other: Operation =>
            throw new CalculatorException("Syntax error in lexem $other.name")
        }
      } catch {
        case ex: CalculatorException =>
          throw ex
        case ex: MatchError =>
          throw new CalculatorException("Wrong parenthesis")
        case ex: NoSuchElementException =>
          throw new CalculatorException("Wrong syntax exception")
        case ex: Exception => println(ex.getClass)
      }
      opStack.remove(opStack.length - 1)
    }
  }
}
