package com.scout24.common.core

import scala.concurrent.Future
import scala.util.Failure

trait ErrorType
case object InputError    extends ErrorType
case object ProcessError  extends ErrorType
case object NotFoundError extends ErrorType
case object UnknownError  extends ErrorType
case object NoError       extends ErrorType

case class ErrorToken(message: String, errorType: ErrorType = UnknownError) extends Throwable

object ErrorToken {

  val empty = ErrorToken("", NoError)

  def failure(message: String, errorType: ErrorType = UnknownError)
    = Failure(ErrorToken(message, errorType))

  def left(message: String, errorType: ErrorType = UnknownError)
    = Left(ErrorToken(message, errorType))

  def future(message: String, errorType: ErrorType = UnknownError)
    = Future.failed(ErrorToken(message, errorType))
}
