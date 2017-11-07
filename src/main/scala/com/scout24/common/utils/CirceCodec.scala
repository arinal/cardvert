package com.scout24.common.utils

import io.circe.{Encoder, Json}
import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure}
import org.joda.time.DateTime
import scala.util.{ Failure, Success, Try }

trait CirceCodec {

  implicit val jodaEncoder: Encoder[DateTime] = Encoder.instance[DateTime] { date =>
    Json.fromString(date.toString)
  }

  implicit val jodaDecoder: Decoder[DateTime] = Decoder.instance { cursor =>
    cursor.focus.map {
      case json if json.isString => parseDate(json.asString.get)
      case json if json.isNumber => json.asNumber match {
        case Some(num) if num.toLong.isDefined => Right(new DateTime(num.toLong.get))
        case _ => Left(DecodingFailure("Failed expecting number as DateTime", cursor.history))
      }
    }.getOrElse(Left(DecodingFailure("DateTime", cursor.history)))
  }

  def parseDate(dateTime: String): Result[DateTime] = Try(DateTime.parse(dateTime)) match {
    case Success(date) => Right(date)
    case Failure(err) => Left(DecodingFailure(err.getMessage, Nil))
  }
}
