package com.scout24.cardvert

import org.joda.time.DateTime
import com.scout24.cardvert.core.advert._
import com.scout24.cardvert.infra.repository.inmemory.AdvertInMemoryRepo

trait InitRepo {

  lazy val advertRepo = new AdvertInMemoryRepo

  val newKijang  = NewCarAdvert (1, "New Toyota Kijang",  Gasoline, 92000)
  val usedKijang = UsedCarAdvert(2, "Used Toyota Kijang", Gasoline, 52000, 889, DateTime.now().minusYears(3))
  val newMaybach = NewCarAdvert (3, "Brand New Maybach",  Gasoline, 660500)
  val mrBeans    = UsedCarAdvert(4, "Mr. Beans' Orange 1969 Mini", Gasoline, 550000, 1000, DateTime.now().minusYears(10))
  val newPanther = NewCarAdvert (5, "New Isuzu Panther Diesel",    Diesel  , 6000)

  val allAdverts = Seq(
    newKijang,
    usedKijang,
    newMaybach,
    mrBeans,
    newPanther)

  def initRepo() = allAdverts.foreach(advertRepo.insert)
}
