package com.scout24.cardvert.core.advert

import com.scout24.common.core.{Entity, InputError}
import org.joda.time.DateTime
import scala.util.{Success, Try}
import scala.language.postfixOps

trait Fuel
case object Gasoline extends Fuel
case object Diesel extends Fuel

trait Advert extends Entity[Int] {
  def id: Int
  def title: String
  def fuel: Fuel
  def price: Int
  def isNew: Boolean
}

case class NewCarAdvert(id: Int, title: String, fuel: Fuel, price: Int) extends Advert {
  override def isNew = true
}

case class UsedCarAdvert(id: Int,
                         title: String,
                         fuel: Fuel,
                         price: Int,
                         mileage: Int,
                         registration: DateTime)
  extends Advert {

  override def isNew = false
}

object Advert {

  import com.scout24.common.core.ErrorToken._

  def validate(advert: Advert): Try[Advert] =
    if (advert.id <= 0) failure("Id must greater than 0", InputError)
    else if (advert.price < 0) failure("Price cannot be negative", InputError)
    else Success(advert)

  def newCarAdvert(id: Int, title: String, fuel: Fuel, price: Int): Try[NewCarAdvert] = {
    val advert = NewCarAdvert(id, title, fuel, price)
    validate(advert).map(_ => NewCarAdvert(advert.id, advert.title, advert.fuel, advert.price))
  }

  def usedCarAdvert(id: Int, title: String, fuel: Fuel, price: Int, mileage: Int, registration: DateTime)
  : Try[UsedCarAdvert] = {
    val advert = UsedCarAdvert(id, title, fuel, price, mileage, registration.withTimeAtStartOfDay)
    validate(advert) flatMap { _ =>
      if (mileage < 0) failure("Mileage cannot be negative", InputError)
      else if (registration isAfterNow) failure("Registration cannot be in the future")
      else Success(advert)
    }
  }
}
