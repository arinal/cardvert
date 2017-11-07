package com.scout24.cardvert.core

import com.scout24.cardvert.core.advert._
import com.scout24.cardvert.infra.repository.inmemory.AdvertInMemoryRepo
import org.joda.time.DateTime

trait InitRepo {

  lazy val advertRepo = new AdvertInMemoryRepo

  val allAdverts = Seq(
    NewCarAdvert(1, "New Toyota Kijang", Gasoline, 92000),
    UsedCarAdvert(2, "Used Toyota Kijang", Gasoline, 52000, 889, DateTime.now().minusYears(3)),
    NewCarAdvert(3, "Brand New Maybach", Gasoline, 660500),
    UsedCarAdvert(4, "Mr. Beans' Orange 1969 Mini", Gasoline, 550000, 1000, DateTime.now().minusYears(10)),
    NewCarAdvert(5, "New Isuzu Panther Diesel", Diesel, 6000),
  )

  def initRepo() = allAdverts.foreach(advertRepo.insert)
}
