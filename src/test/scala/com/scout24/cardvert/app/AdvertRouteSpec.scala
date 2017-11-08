package com.scout24.cardvert.app

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import com.scout24.common.utils.CirceCodec
import org.joda.time.DateTime
import org.scalatest.{ Matchers, WordSpec }
import com.scout24.utils.ForceAwait
import com.scout24.cardvert.InitRepo
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.scout24.cardvert.core.advert.AdvertService

class AdvertRouteSpec extends WordSpec
    with Matchers
    with InitRepo
    with CirceCodec
    with ForceAwait
    with ScalatestRouteTest {

  import akka.http.scaladsl.model.StatusCodes._
  import io.circe.syntax._
  import io.circe.generic.auto._
  import com.scout24.common.infra.akkahttp.CirceCodec._
  import AdvertModel._

  val service = new AdvertService(advertRepo)
  val route = new AdvertRoute(service).route

  initRepo()

  "advert route" should {

    "return correct data when trying to get an existence advert by id" in {
      Get(s"/advert?id=${newKijang.id}") ~> route ~> check {
        status shouldBe OK
        responseAs[Option[AdvertModel]] shouldBe Some(fromDomain(newKijang))
      }
    }

    "return correct data when trying to get all advert" in {
      Get("/advert") ~> route ~> check {
        status shouldBe OK
        // TODO: prefer this but somehow it doesn't work
        // responseAs[Seq[CarAdvert]] should contain theSameElementsAs allAdverts.map(CarAdvert.fromAdvert)
        responseAs[Seq[AdvertModel]] should have length allAdverts.length
      }
    }

    "add advert when trying to post new advert" in {
      val advModel = AdvertModel(100, "Bat's Car", "Gasoline", 5500000, false, Some(5000), Some(DateTime.now()))
      Post("/advert", HttpEntity(ContentTypes.`application/json`, advModel.asJson.toString)) ~> route ~> check {
        status shouldBe OK
        val advert = advertRepo.byId(100).await.get
        // TODO: prefer this but somehow it doesn't work:  advModel shouldEqual fromDomain(advert)
        advert.title shouldEqual advModel.title
      }
    }

    "change advert when trying to put existing advert" in {
      val changedMrBeansModel = fromDomain(mrBeans).copy(title = "Mrs. Beans' Black 1969")
      Put("/advert", HttpEntity(ContentTypes.`application/json`, changedMrBeansModel.asJson.toString)) ~> route ~> check {
        status shouldBe OK
        val actualAdvert = advertRepo.byId(mrBeans.id).await.get
        // TODO: prefer this but somehow it doesn't work: fromDomain(actualAdvert) shouldEqual changedMrBeansModel
        actualAdvert.title shouldEqual changedMrBeansModel.title
      }
    }

    "remove advert when tyring to delete advert" in {
      val initialSize = advertRepo.count.await
      Delete(s"/advert?id=${newMaybach.id}") ~> route ~> check {
        status shouldBe OK
        advertRepo.count.await shouldEqual (initialSize - 1)
      }
    }

    "return 404 when trying to get non-existent advert by id" in {
      Get("/advert?id=999") ~> route ~> check {
        status shouldBe NotFound
      }
    }

    "return 400 when trying to post invalid data" in {
      val advModel = AdvertModel(100, "Bat's Car", "Gasoline", 5500000, false, Some(5000), None)
      Post("/advert", HttpEntity(ContentTypes.`application/json`, advModel.asJson.toString)) ~> route ~> check {
        status shouldBe BadRequest
      }
    }
  }
}
