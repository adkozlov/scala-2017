package ru.spbau.mit

class Parser() {
  private val patternNumber = "^([\\d]*\\.?[\\d]+)(.*)".r
  private val patternKeyWords =
    ("^(" + ExpressionElements.keyWords.mkString("|") + ")(.*)").r
  private val patternKeyWordsWithArgs =
    ("^(" + ExpressionElements.keyWordsWithArgs.mkString("|") + ")(.*)").r
  private val patternConstants =
    ("^(" + ExpressionElements.constants.mkString("|") + ")(.*)").r
  def parseLexems(expr: String): List[Any] = {
    expr match {
      case "" => Nil
      case `expr` if expr.startsWith(" ") => parseLexems(expr.substring(1))
      case patternNumber(number, rest) => number.toDouble :: parseLexems(rest.toString)
      case `expr` if expr.startsWith(ExpressionElements.openBracket)
                  => new OpenBracket() :: parseLexems(expr.substring(ExpressionElements.openBracket.length))
      case `expr` if expr.startsWith(ExpressionElements.closeBracket)
                  => new CloseBracket() :: parseLexems(expr.substring(ExpressionElements.closeBracket.length))
      case patternKeyWords(keyWord, rest) => ExpressionElements.classes(keyWord) :: parseLexems(rest)
      case patternKeyWordsWithArgs(keyWordWithArg, rest) => ExpressionElements.classes(keyWordWithArg) :: parseLexems(rest)
      case patternConstants(constant, rest) => ExpressionElements.classes(constant) :: parseLexems(rest)
      case _ => throw new IllegalArgumentException(s"Can't parse string $expr")
    }
  }

}
