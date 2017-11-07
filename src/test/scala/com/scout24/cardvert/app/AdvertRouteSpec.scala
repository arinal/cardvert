package com.scout24.cardvert.app

import com.scout24.common.utils.CirceCodec
import org.scalatest.{ Matchers, WordSpec }
import com.scout24.cardvert.InitRepo
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.scout24.cardvert.core.advert.AdvertService

class AdvertRouteSpec extends WordSpec
    with Matchers
    with InitRepo
    with CirceCodec
    with ScalatestRouteTest {

  import akka.http.scaladsl.model.StatusCodes._
  import io.circe.generic.auto._
  import com.scout24.common.infra.akkahttp.CirceCodec._

  val service = new AdvertService(advertRepo)
  val route = new AdvertRoute(service).route

  initRepo()

  "advert route" should {
    "get an existence advert by id" in {
      Get("/advert?id=1") ~> route ~> check {
        status shouldEqual OK
      }
    }

    "get non-existent advert by id" in {
      Get("/advert?id=100") ~> route ~> check {
        status shouldEqual NotFound
      }
    }

    "you" in {
      Get("/advert") ~> route ~> check {
        status shouldEqual OK
        // responseAs[Seq[CarAdvert]] should contain theSameElementsAs allAdverts.map(CarAdvert.fromAdvert)
      }
    }
  }
}
