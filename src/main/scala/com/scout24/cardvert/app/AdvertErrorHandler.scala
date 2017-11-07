package com.scout24.cardvert.app

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}

trait AdvertErrorHandler {

  import com.scout24.common.core._

  lazy val errorHandler = ExceptionHandler {
    case token@ ErrorToken(message, errorType) =>
      extractUri { uri =>
        val status = deductStatus(token)
        println(s"Request to $uri could not be handled normally.\nError: $message")
        complete(HttpResponse(status, entity = message))
      }
  }

  private def deductStatus(token: ErrorToken) = token.errorType match {
    case NotFoundError => StatusCodes.NotFound
    case InputError    => StatusCodes.BadRequest
    case ProcessError | UnknownError => StatusCodes.InternalServerError
  }
}
