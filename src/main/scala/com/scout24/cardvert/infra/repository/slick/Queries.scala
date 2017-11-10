package com.scout24.cardvert.infra.repository.slick

import com.scout24.common.infra.repo.slick.SlickProfile
import org.joda.time.DateTime

case class AdvertDb(id: Int,
                    title: String,
                    fuel: Int,
                    price: Int,
                    isNew: Boolean,
                    mileage: Option[Int],
                    registration: Option[DateTime])

trait Queries extends SlickProfile {

  import profile.api._
  object JodaSupport extends com.github.tototoshi.slick.GenericJodaSupport(profile)
  import JodaSupport._

  class AdvertTable(tag: Tag) extends Table[AdvertDb](tag, "Adverts") {
    def id           = column[Int]("id", O.PrimaryKey)
    def title        = column[String]("title")
    def fuel         = column[Int]("fuel")
    def price        = column[Int]("price")
    def isNew        = column[Boolean]("is_new")
    def mileage      = column[Option[Int]]("mileage")
    def registration = column[Option[DateTime]]("registration")

    def * = (id, title, fuel, price, isNew, mileage, registration).mapTo[AdvertDb]
  }

  object AdvertQuery extends TableQuery(new AdvertTable(_)) {

    def countAct = size.result
    def allAct   = this.result
    def findAct(id: Int) = find(id).result.head

    def updateAct(advert: AdvertDb) = find(advert.id)
      .map(m => (m.id, m.title, m.fuel, m.price, m.isNew, m.mileage, m.registration))
      .update((advert.id, advert.title, advert.fuel, advert.price, advert.isNew, advert.mileage, advert.registration))

    def find(id: Int) = filter(_.id === id)
  }
}
