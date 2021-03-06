package com.scout24.common.infra.akkahttp

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpEncodings
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import io.circe.Decoder
import io.circe.parser.decode

object AkkaHttpClient {
  case class ResponseError(response: HttpResponse) extends Throwable
}

trait AkkaHttpClient {

  type CommonFlow = Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]]
  import AkkaHttpClient._

  def httpPost(uri: String, body: String, headers: Seq[HttpHeader] = Nil)
                 (implicit as: ActorSystem, mat: Materializer) = {
    val request = HttpRequest(uri = uri,
      method = HttpMethods.POST,
      headers = headers,
      entity = HttpEntity(body))
    Http().singleRequest(request)
  }

  def httpCallAndDecode[A](uri: String, headers: Seq[HttpHeader] = Nil)
                 (implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext, dec: Decoder[A]): Future[A] =
    httpCallAndDecode[A](HttpRequest(uri = uri, headers = headers))

  def httpCallAndDecode[A](request: HttpRequest)
              (implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext, dec: Decoder[A]) =
    for {
      resp <- Http().singleRequest(request)
      _    <- if (resp.status.intValue >= 300) Future.failed(ResponseError(resp))
              else Future.successful(resp)

      rawData  = resp.entity.dataBytes.runFold(ByteString.empty)(_ ++ _)
      encoding = resp.getHeader("Content-Encoding")
      data <- if (encoding.isPresent && encoding.get.value == HttpEncodings.gzip.value) rawData.flatMap(Gzip.decode)
              else rawData

      decoded <- decode[A](data.utf8String) match {
             case Right(value) => Future.successful(value)
             case Left(err) => Future.failed(err)
           }
    } yield decoded
}
