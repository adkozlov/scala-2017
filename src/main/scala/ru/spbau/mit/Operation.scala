package ru.spbau.mit

abstract class Operation(name: String) {
  def getPriority: Int = 0

  override def equals(obj: scala.Any): Boolean = obj.getClass == this.getClass
}

abstract class BinaryOperation(name: String) extends Operation(name) {
  def eval(firstOp: Double, secondOp: Double): Double
}

abstract class UnaryOperation(name: String) extends Operation(name) {
  def eval(operand: Double): Double
}

abstract class Constant(name: String) extends Operation(name) {
  def eval(): Double
}

class One extends Constant("One") {
  override def eval(): Double = 1

  override def getPriority: Int = 8
}

class Add() extends BinaryOperation("Add") {
  override def eval(firstOp: Double, secondOp: Double): Double = firstOp + secondOp
}

class Subtract() extends BinaryOperation("Substract") {
  override def eval(firstOp: Double, secondOp: Double): Double = firstOp - secondOp
}

class Product() extends BinaryOperation("Mult") {
  override def eval(firstOp: Double, secondOp: Double): Double = firstOp * secondOp

  override def getPriority: Int = 5
}

class Divide() extends BinaryOperation("Divide") {
  override def eval(firstOp: Double, secondOp: Double): Double = firstOp / secondOp

  override def getPriority: Int = 5
}

class Sin() extends UnaryOperation("Sinus") {
  override def eval(operand: Double): Double = math.sin(operand)

  override def getPriority: Int = 8
}

class Cos() extends UnaryOperation("Cosinus") {
  override def eval(operand: Double): Double = math.cos(operand)

  override def getPriority: Int = 8
}

class OpenBracket() extends Operation("Open bracket") {
  override def getPriority: Int = 10
}

class CloseBracket() extends Operation("Close bracket") {
  override def getPriority: Int = 10
}