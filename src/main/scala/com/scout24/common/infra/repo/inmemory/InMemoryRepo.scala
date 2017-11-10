package com.scout24.common.infra.repo.inmemory

import scala.collection.mutable
import scala.concurrent.Future
import com.scout24.common.core.{Entity, Repository}

abstract class InMemoryRepo[E <: Entity[Id], Id] extends Repository[E, Id] {

  import com.scout24.common.core.ErrorToken._

  private lazy val entityMap = mutable.Map[Id, E]()

  override def count: Future[Int]  = Future.successful(entityMap.size)
  override def all: Future[Seq[E]] = Future.successful(entityMap.values.toSeq)

  override def byId(id: Id): Future[Option[E]] = Future.successful(entityMap.get(id))
  override def delete(id: Id): Future[Unit]    = Future.successful(entityMap -= id)
  override def update(entity: E): Future[Unit] = Future.successful(entityMap += entity.id -> entity)

  override def insert(entity: E): Future[Unit] =
    if (!entityMap.contains(entity.id)) Future.successful(entityMap += entity.id -> entity)
    else future(alreadyExistsError(entity))
}
