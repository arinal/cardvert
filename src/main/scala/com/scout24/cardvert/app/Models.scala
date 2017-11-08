package com.scout24.cardvert.app

import scala.util.{Try, Success}
import com.scout24.cardvert.core.advert.{ Advert, NewCarAdvert, UsedCarAdvert, Fuel, Gasoline, Diesel }
import org.joda.time.DateTime

case class AdvertModel(id: Int,
                       title: String,
                       fuel: String,
                       price: Int,
                       isNew: Boolean,
                       mileage: Option[Int],
                       registration: Option[DateTime])

object AdvertModel {

  import com.scout24.common.core.ErrorToken._
  import com.scout24.common.utils.Syntax._
  import com.scout24.common.core.ErrorToken._

  def toString(fuel: Fuel) = fuel match {
    case Gasoline => "Gasoline"
    case Diesel   => "Diesel"
  }

  def toFuel(fuel: String) = fuel match {
    case "Gasoline" => Gasoline
    case "Diesel"   => Diesel
  }

  def toDomain(model: AdvertModel) = {
    import model._
    for (valid <- validate(model))
    yield if (model.isNew) NewCarAdvert(id, title, toFuel(fuel), price)
          else UsedCarAdvert(id, title, toFuel(fuel), price, mileage.get, registration.get)
  }

  def validate(model: AdvertModel): Try[AdvertModel] = {
    import model._
    if (model.isNew) Success(model)
    else for {
      mil <- mileage.toTry(
        ifFail = inputError("Mileage shouldn't be empty for used car"))
      reg <- registration.toTry(
        ifFail = inputError("Registration shouldn't be empty for used car"))
    } yield model
  }

  def fromDomain(advert: Advert) = {
    advert match {
      case NewCarAdvert(id, title, fuel, price) =>
        AdvertModel(id, title, toString(fuel), price, true, None, None)
      case UsedCarAdvert(id, title, fuel, price, mil, reg) =>
        AdvertModel(id, title, toString(fuel), price, false, Some(mil), Some(reg))
    }
  }
}
