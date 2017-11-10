package com.scout24.cardvert.infra.repository.slick

import com.scout24.cardvert.core.advert.{Advert, Diesel, Fuel, Gasoline, NewCarAdvert, UsedCarAdvert}

trait AdvertDbTransformer {

  def toDomain(advertDb: AdvertDb) = {
    import advertDb._
    if (isNew) NewCarAdvert(id, title, toFuel(fuel), price)
    else UsedCarAdvert(id, title, toFuel(fuel), price, mileage.get, registration.get)
  }

  def fromDomain(advert: Advert): AdvertDb = advert match {
    case NewCarAdvert(id, title, fuel, price) =>
      AdvertDb(id, title, toFuelDb(fuel), price, true, None, None)
    case UsedCarAdvert(id, title, fuel, price, mil, reg) =>
      AdvertDb(id, title, toFuelDb(fuel), price, false, Some(mil), Some(reg))
  }

  def toFuelDb(fuel: Fuel) = fuel match {
    case Gasoline => 1
    case Diesel   => 2
  }

  def toFuel(fuel: Int) = fuel match {
    case 1 => Gasoline
    case 2 => Diesel
  }
}
