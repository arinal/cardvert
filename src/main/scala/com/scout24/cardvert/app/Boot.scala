package com.scout24.cardvert.app

import com.scout24.cardvert.app.injection.Injection

object Boot extends App
    with Injection {

  injectionInit()

  val binding = restServer.start()

  scala.sys.addShutdownHook {
    restServer.stop(binding)
    println("Server stopped...")
  }
}
