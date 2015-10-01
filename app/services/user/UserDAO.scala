package services.user

import models.{UniqueId, User}

trait UserDAO {
  def add(user: User): Unit
  def remove(user: User): Unit
  def exists(userId: UniqueId): Boolean
  def existsByName(username: String): Boolean
  def retrieve(userId: UniqueId): Option[User]
  def retrieveByName(username: String): Option[User]
}
