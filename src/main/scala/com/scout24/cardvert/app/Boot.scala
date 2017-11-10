package com.scout24.cardvert.app

import akka.http.scaladsl.server.HttpApp
import com.scout24.cardvert.app.injection.Injection

object Boot extends HttpApp
    with App
    with Injection {

  injectionInit()
  startServer(config.httpHost, config.httpPort)
}
