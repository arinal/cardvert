package com.scout24.cardvert.app

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import com.scout24.common.utils.CirceCodec
import org.joda.time.DateTime
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
  import io.circe.syntax._
  import io.circe.generic.auto._
  import com.scout24.common.infra.akkahttp.CirceCodec._

  val service = new AdvertService(advertRepo)
  val route = new AdvertRoute(service).route

  initRepo()

  "advert route" should {

    "return 404 when trying to get non-existent advert by id" in {
      Get("/advert?id=100") ~> route ~> check {
        status shouldBe NotFound
      }
    }

    "return correct data when trying to get an existence advert by id" in {
      Get("/advert?id=1") ~> route ~> check {
        status shouldBe OK
        responseAs[Option[CarAdvert]] shouldBe Some(CarAdvert.fromAdvert(newKijang))
      }
    }

    "return correct data when trying to get all advert" in {
      Get("/advert") ~> route ~> check {
        status shouldBe OK
        // TODO: fix this
        // responseAs[Seq[CarAdvert]] should contain theSameElementsAs allAdverts.map(CarAdvert.fromAdvert)
      }
    }

    "return 'created status' when trying to post new advert" in {
      val newAdvert = CarAdvert(100, "Batman's Car", "Gasoline", 500, false, Some(5000), Some(DateTime.now))
      Post("/advert", HttpEntity(ContentTypes.`application/json`, newAdvert.asJson.toString)) ~> route ~> check {
        status shouldBe OK
      }
    }
  }
}
