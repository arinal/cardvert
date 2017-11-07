package com.scout24.cardvert.app

object Boot extends App
    with Injection {

  val binding = restServer.start()

  scala.sys.addShutdownHook {
    restServer.stop(binding)
    println("Server stopped...")
  }
}
