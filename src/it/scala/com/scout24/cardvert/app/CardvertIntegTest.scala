package com.scout24.cardvert.app

import com.scout24.cardvert.app

import org.scalatest.{AsyncFlatSpec, Matchers}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.scout24.common.utils.CirceCodec
import com.scout24.common.infra.akkahttp.AkkaHttpClient
import com.scout24.cardvert.core.advert._

class CardvertIntegTest extends AsyncFlatSpec
    with Matchers
    with AkkaHttpClient
    with CirceCodec {

  import io.circe.syntax._
  import io.circe.generic.auto._
  import akka.http.scaladsl.model.StatusCodes._

  implicit val _sys = ActorSystem("integration-test")
  implicit val _ec = _sys.dispatcher
  implicit val _mat: Materializer = ActorMaterializer()

  val baseUrl = "http://localhost:8080/advert"

  "Doing CRUD in happy path manner" should
  "behave accordingly" in {

    for {
      newKijang  <- httpCallAndDecode[AdvertModel](s"$baseUrl?id=1")
      usedKijang <- httpCallAndDecode[AdvertModel](s"$baseUrl?id=2")
      all        <- httpCallAndDecode[Seq[AdvertModel]](s"$baseUrl")
    } yield (newKijang, usedKijang, all)
      .map { case (nk, uk, all) =>
        nk.title   shouldBe "New Toyota Kijang"
        uk.title   shouldBe "Used Toyota Kijang"
        all.length shouldBe 5
      }
  }
}
