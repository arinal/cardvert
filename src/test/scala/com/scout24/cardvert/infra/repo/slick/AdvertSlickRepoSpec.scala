package com.scout24.cardvert.infra.repository.slick

import com.scout24.common.core.ErrorToken
import com.scout24.cardvert.InitRepo
import org.scalatest.{AsyncFlatSpec, Matchers}

class AdvertSlickRepoSpec extends AsyncFlatSpec
    with Matchers
    with InitRepo {

  override lazy val advertRepo = new AdvertSlickRepo {
    override lazy val profile = slick.jdbc.H2Profile
    import slick.jdbc.H2Profile.api._
    override lazy val db = Database.forConfig("db.h2")

    mkTable()
  }

  "find advert in empty repository" should
  "return nothing" in {
    advertRepo.count.map(_ shouldEqual 0)
    advertRepo.byId(newKijang.id).map(_ shouldEqual None)
  }

  "inserting advert, retrieving count and get all adverts" should
  "return correct count and adverts" in {
    initRepo()
    advertRepo.count.map(_ shouldEqual allAdverts.length)
    advertRepo.all.map(_ shouldEqual allAdverts)
    advertRepo.byId(newPanther.id)
      .map(_ shouldEqual Some(newPanther))
  }

  "updating 'Toyota Kijang' to Toyota Jangkrik'" should
  "changed the title" in {
    val changed = usedKijang.copy(title = "Used Toyota Jangkrik")
    advertRepo.update(changed)
    advertRepo.byId(usedKijang.id)
      .map(_ shouldEqual Some(changed))
  }

  "inserting existing advertisement" should
  "return already exists error" in {
    recoverToSucceededIf[ErrorToken] {
      advertRepo.insert(usedKijang)
    }
  }
}
