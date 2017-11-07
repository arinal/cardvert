package com.scout24.cardvert.core.advert

import com.scout24.cardvert.core.AdvertsAlg

import scala.concurrent.Future

class AdvertService(advertRepo: AdvertRepository) extends AdvertsAlg[Advert, Int] {

  override def getAll: Future[Seq[Advert]]              = advertRepo.all
  override def getById(id: Int): Future[Option[Advert]] = advertRepo.byId(id)

  override def add(advert: Advert): Future[Unit]    = advertRepo.insert(advert)
  override def update(advert: Advert): Future[Unit] = advertRepo.update(advert)
  override def delete(id: Int): Future[Unit]        = advertRepo.delete(id)
}
