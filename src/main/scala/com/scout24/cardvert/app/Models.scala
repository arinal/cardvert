package com.scout24.cardvert.app

import com.scout24.cardvert.core.advert.{ Advert, NewCarAdvert, UsedCarAdvert, Fuel }
import org.joda.time.DateTime

case class Damn(mil: Option[DateTime])
case class CarAdvert(id: Int,
                     title: String,
                     fuel: Fuel,
                     price: Int,
                     isNew: Boolean,
                     mileage: Option[Int],
                     registration: Option[DateTime])

object CarAdvert {

  def fromAdvert(advert: Advert) = {
    advert match {
      case NewCarAdvert(id, title, fuel, price) =>
        CarAdvert(id, title, fuel, price, false, None, None)
      case UsedCarAdvert(id, title, fuel, price, mil, reg) =>
        CarAdvert(id, title, fuel, price, true, Some(mil), Some(reg))
    }
  }
}
