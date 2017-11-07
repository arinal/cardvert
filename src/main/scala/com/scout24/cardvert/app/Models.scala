package com.scout24.cardvert.app

import com.scout24.cardvert.core.advert.{ Advert, NewCarAdvert, UsedCarAdvert, Fuel, Gasoline, Diesel }
import org.joda.time.DateTime

case class CarAdvert(id: Int,
                     title: String,
                     fuel: String,
                     price: Int,
                     isNew: Boolean,
                     mileage: Option[Int],
                     registration: Option[DateTime])

object CarAdvert {

  def toString(fuel: Fuel) = fuel match {
    case Gasoline => "Gasoline"
    case Diesel => "Diesel"
  }

  def fromAdvert(advert: Advert) = {
    advert match {
      case NewCarAdvert(id, title, fuel, price) =>
        CarAdvert(id, title, toString(fuel), price, false, None, None)
      case UsedCarAdvert(id, title, fuel, price, mil, reg) =>
        CarAdvert(id, title, toString(fuel), price, true, Some(mil), Some(reg))
    }
  }
}
