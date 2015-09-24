package services.user

import models.User

trait UserDAO {
  def add(user: User): Unit
  def remove(user: User): Unit
  def exists(username: String): Boolean
  def retrieve(username: String, password: String): Option[User]
}
