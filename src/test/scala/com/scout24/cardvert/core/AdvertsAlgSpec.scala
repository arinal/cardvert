package com.scout24.cardvert.core

import com.scout24.cardvert.InitRepo
import com.scout24.cardvert.core.advert.AdvertService
import org.scalatest.{AsyncFlatSpec, Matchers}

class AdvertsAlgSpec extends AsyncFlatSpec
    with Matchers
    with InitRepo {

  val service = new AdvertService(advertRepo)

  initRepo()

  "find non-existent advertisement" should
  "return none" in {
    service.getById(101) map (_ shouldBe None)
  }

  "find all advertisements" should
  "return all advertisements" in {
    service.getAll map (_ should contain theSameElementsAs allAdverts)
  }
}
