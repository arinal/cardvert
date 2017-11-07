package com.scout24.cardvert.infra.repository.inmemory

import com.scout24.cardvert.core.advert.{Advert, AdvertRepository}
import com.scout24.common.infra.repo.inmemory.InMemoryRepo

class AdvertInMemoryRepo extends InMemoryRepo[Advert, Int] with AdvertRepository
