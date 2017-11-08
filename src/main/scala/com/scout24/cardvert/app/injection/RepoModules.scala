package com.scout24.cardvert.app.injection

import org.joda.time.DateTime
import com.softwaremill.macwire.wire
import com.scout24.cardvert.core.advert._
import com.scout24.cardvert.infra.repository.inmemory.AdvertInMemoryRepo

class InMemoryRepoModule {

  lazy val advertRepo = wire[AdvertInMemoryRepo]

  def init() = {
    val now = DateTime.now()
    Seq(NewCarAdvert (1, "New Toyota Kijang",  Gasoline, 92000),
        UsedCarAdvert(2, "Used Toyota Kijang", Gasoline, 52000, 889, now.minusYears(3)),
        NewCarAdvert (3, "Brand New Maybach",  Gasoline, 660500),
        UsedCarAdvert(4, "Mr. Beans' Orange 1969 Mini", Gasoline, 550000, 1000, now.minusYears(10)),
        NewCarAdvert (5, "New Isuzu Panther Diesel",    Diesel  , 6000))
      .foreach(advertRepo.insert)
  }
}
