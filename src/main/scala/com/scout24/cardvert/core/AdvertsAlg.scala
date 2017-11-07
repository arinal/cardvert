package com.scout24.cardvert.core

import scala.concurrent.Future

trait AdvertsAlg[Advert, Id] {
  def getAll: Future[Seq[Advert]]
  def getById(id: Id): Future[Option[Advert]]

  def add(advert: Advert): Future[Unit]
  def update(advert: Advert): Future[Unit]
  def delete(id: Id): Future[Unit]
}
