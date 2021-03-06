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

  def inputError(message: String) = ErrorToken(message, InputError)
  def notFoundError(message: String) = ErrorToken(message, NotFoundError)

  def failure(message: String, errorType: ErrorType = UnknownError)
    = Failure(ErrorToken(message, errorType))

  def left(message: String, errorType: ErrorType = UnknownError)
    = Left(ErrorToken(message, errorType))

  def future(error: ErrorToken) = Future.failed(error)

  def future(message: String, errorType: ErrorType = UnknownError): Future[ErrorToken]
    = future(ErrorToken(message, errorType))
}
