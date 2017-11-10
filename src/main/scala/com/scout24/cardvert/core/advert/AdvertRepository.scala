package com.scout24.cardvert.core.advert

import com.scout24.common.core.Repository
import com.scout24.common.core.ErrorToken

trait AdvertRepository extends Repository[Advert, Int] {

  import com.scout24.common.core.ErrorToken._

  override def alreadyExistsError(advert: Advert): ErrorToken =
    inputError(s"Advert with code ${advert.id} already exists")
}
