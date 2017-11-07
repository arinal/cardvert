package com.scout24.cardvert.app

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.scout24.cardvert.core.advert.{ NewCarAdvert, UsedCarAdvert }
import com.scout24.common.utils.CirceCodec
import scala.concurrent.ExecutionContext
import com.scout24.cardvert.core.advert.AdvertService

class AdvertRoute(service: AdvertService)
                 (implicit system: ActorSystem, _mat: Materializer, _ec: ExecutionContext)
    extends AdvertErrorHandler
    with CirceCodec {

  import io.circe.generic.auto._
  import com.scout24.common.infra.akkahttp.CirceCodec._
  import com.scout24.common.core._
  import com.scout24.common.utils.Syntax._

  lazy val route: Route = pathPrefix("advert") {
    handleExceptions(errorHandler) {
      get {
        (parameter('id.as[Int])) { id =>
          complete {
            for {
              maybe <- service.getById(id)
              adv   <- maybe.toFuture(ErrorToken(s"Advert with id $id not found", NotFoundError))
            } yield CarAdvert.fromAdvert(adv)
          }
        }
      }
    }
  }
}

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
