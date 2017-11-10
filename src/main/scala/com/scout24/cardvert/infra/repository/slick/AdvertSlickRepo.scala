package com.scout24.cardvert.infra.repository.slick

import com.scout24.cardvert.core.advert.Advert
import scala.concurrent.Future
import org.h2.jdbc.JdbcSQLException
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.scout24.common.infra.repo.slick.SlickProfile
import com.scout24.cardvert.core.advert.AdvertRepository

trait AdvertSlickRepo extends AdvertRepository
    with SlickProfile
    with Queries
    with AdvertDbTransformer {

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

  override def delete(id: Int): Future[Unit] = db.run {
    AdvertQuery.find(id).delete
  }.map(_ => ())

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
}
