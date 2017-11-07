package com.scout24.common.core

import scala.concurrent.Future

trait Repository[E <: Entity[_], Id] {
  def count: Future[Int]
  def byId(id: Id): Future[Option[E]]
  def all: Future[Seq[E]]

  def insert(entity: E): Future[Unit]
  def update(entity: E): Future[Unit]
  def delete(id: Id):    Future[Unit]
}
