package com.scout24.cardvert.app.injection

import com.typesafe.config.ConfigFactory

case class CardvertConfig(httpHost: String, httpPort: Int)

object CardvertConfig {

  def fromFile = {
    val config = ConfigFactory.load()
    CardvertConfig(
      config.getString("http.host"),
      config.getInt("http.port")
    )
  }
}
