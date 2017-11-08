package com.scout24.cardvert.app

import com.softwaremill.macwire.wire
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.scout24.common.infra.akkahttp.RestServer
import com.scout24.cardvert.core.advert.AdvertService
import com.scout24.cardvert.infra.repository.inmemory.AdvertInMemoryRepo

trait Injection {

  implicit val system = ActorSystem("moveon-rest")
  implicit val _ec    = system.dispatcher
  implicit val _mat   = ActorMaterializer()

  lazy val advertRepo: AdvertInMemoryRepo = wire[AdvertInMemoryRepo]
  lazy val service   : AdvertService      = wire[AdvertService]

  lazy val advertRoute: AdvertRoute       = wire[AdvertRoute]
  lazy val route                          = advertRoute.route
  lazy val restServer                     = wire[RestServer]
}
