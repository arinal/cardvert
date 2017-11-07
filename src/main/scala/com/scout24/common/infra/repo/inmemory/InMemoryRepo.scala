package com.scout24.common.infra.repo.inmemory

import com.scout24.common.core.{Entity, Repository}

import scala.collection.mutable
import scala.concurrent.Future

class InMemoryRepo[E <: Entity[Id], Id] extends Repository[E, Id] {

  private lazy val entityMap = mutable.Map[Id, E]()

  override def count: Future[Int]  = Future.successful(entityMap.size)
  override def all: Future[Seq[E]] = Future.successful(entityMap.values.toSeq)

  override def byId(id: Id): Future[Option[E]] = Future.successful(entityMap.get(id))
  override def delete(id: Id): Future[Unit]    = Future.successful(entityMap -= id)
  override def update(entity: E): Future[Unit] = insert(entity)
  override def insert(entity: E): Future[Unit] = {
    entityMap += entity.id -> entity
    Future.successful(entity)
  }
}
