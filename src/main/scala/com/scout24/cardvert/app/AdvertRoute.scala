package com.scout24.cardvert.app

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
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
        } ~ {
          complete {
            for (all <- service.getAll)
            yield all.map(CarAdvert.fromAdvert)
          }
        }
      }
    }
  }
}

