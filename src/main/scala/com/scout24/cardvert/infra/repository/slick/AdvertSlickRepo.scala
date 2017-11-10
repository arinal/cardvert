package com.scout24.cardvert.infra.repository.slick

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

  override def update(entity: Advert): Future[Unit] = ???
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
    else UsedCarAdvert(id, title, toFuel(fuel), price, mileage, toDateTime(registration))
  }

  private def fromDomain(advert: Advert): AdvertDb = {
    ???
  }

  private def toFuel(fuel: Int) = fuel match {
    case 1 => Gasoline
    case 2 => Diesel
  }

  private def toDateTime(date: Date) = ???
}
