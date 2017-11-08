package com.scout24.cardvert.app

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.scout24.common.utils.CirceCodec
import scala.concurrent.ExecutionContext
import com.scout24.cardvert.core.advert.AdvertService

class AdvertRoute(service: AdvertService)
                 (implicit _ec: ExecutionContext)
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
              adv   <- maybe.toFuture(ifFail = notFoundError(id))
            } yield CarAdvert.fromAdvert(adv)
          }
        } ~ {
          complete {
            for (all <- service.getAll)
            yield all.map(CarAdvert.fromAdvert)
          }
        }
      } ~
        post {
          complete("OK")
        }
    }
  }

  def notFoundError(id: Int)
    = ErrorToken(s"Advert with id $id not found", NotFoundError)
}
