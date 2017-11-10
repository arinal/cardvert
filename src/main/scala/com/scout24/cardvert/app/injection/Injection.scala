package com.scout24.cardvert.app.injection

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.scout24.cardvert.app.AdvertRoute
import com.scout24.cardvert.core.advert.AdvertService
import com.softwaremill.macwire.wire

trait Injection {

  implicit val system = ActorSystem("cardvert-rest")
  implicit val _ec    = system.dispatcher
  implicit val _mat   = ActorMaterializer()

  val config = CardvertConfig.fromFile

  lazy val repoModule =
    // new InMemoryRepoModule
    // new H2RepoModule
    new MySqlRepoModule
  import repoModule._

  lazy val service    : AdvertService = wire[AdvertService]
  lazy val advertRoute: AdvertRoute   = wire[AdvertRoute]
  lazy val routes                     = advertRoute.route

  def injectionInit() = repoModule.init()
}
