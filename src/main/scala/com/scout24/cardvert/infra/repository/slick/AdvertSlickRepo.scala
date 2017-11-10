package com.scout24.cardvert.infra.repository.slick

import com.scout24.cardvert.core.advert.Fuel
import org.joda.time.DateTime
import scala.concurrent.Future
import java.sql.Date
import org.h2.jdbc.JdbcSQLException
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.scout24.common.infra.repo.slick.SlickProfile
import com.scout24.cardvert.core.advert.AdvertRepository
import com.scout24.cardvert.core.advert.{ Advert, NewCarAdvert, UsedCarAdvert, Gasoline, Diesel }

trait AdvertSlickRepo extends AdvertRepository
    with SlickProfile
    with Queries {

  import profile.api._

  override def count: Future[Int]         = db.run(AdvertQuery.countAct)
  override def all  : Future[Seq[Advert]] = db.run(AdvertQuery.allAct).map(_.map(toDomain))

  override def byId(id: Int): Future[Option[Advert]] =
    db.run(AdvertQuery.findAct(id))
      .map(toDomain)
      .map(Option.apply)
      .recover {
        case _: NoSuchElementException => None
      }

  override def update(advert: Advert): Future[Unit] = db.run {
    AdvertQuery.updateAct(fromDomain(advert))
  }.map(_ => ())

  override def delete(id: Int)   : Future[Unit] = ???

  override def insert(advert: Advert): Future[Unit] =
    db.run(AdvertQuery += fromDomain(advert))
      .map(_ => ())
      .recoverWith {
        case ex: JdbcSQLException
            if ex.getMessage.contains("Unique index or primary key") =>
          Future.failed(alreadyExistsError(advert))
        case _: MySQLIntegrityConstraintViolationException =>
          Future.failed(alreadyExistsError(advert))
      }

  def mkTable() = db.run(AdvertQuery.schema.create)

  private def toDomain(advertDb: AdvertDb) = {
    import advertDb._
    if (isNew) NewCarAdvert(id, title, toFuel(fuel), price)
    else UsedCarAdvert(id, title, toFuel(fuel), price, mileage.get, toDateTime(registration.get))
  }

  private def fromDomain(advert: Advert): AdvertDb = advert match {
    case NewCarAdvert(id, title, fuel, price) =>
      AdvertDb(id, title, toFuelDb(fuel), price, true, None, None)
    case UsedCarAdvert(id, title, fuel, price, mil, reg) =>
      AdvertDb(id, title, toFuelDb(fuel), price, false, Some(mil), Some(toSqlDate(reg)))
  }

  private def toFuelDb(fuel: Fuel) = fuel match {
    case Gasoline => 1
    case Diesel   => 2
  }

  private def toFuel(fuel: Int) = fuel match {
    case 1 => Gasoline
    case 2 => Diesel
  }

  private def toDateTime(date: Date) = DateTime.parse(date.toString)
  private def toSqlDate(date: DateTime) = Date.valueOf(date.toString("yyyy-mm-dd"))
}
