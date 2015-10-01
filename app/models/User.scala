package models

case class User(username: String, password: String, uuid: UniqueId)


object User {
  def apply(username: String, password: String): User = {
    User(username, password, UniqueIdGenerator.generate)
  }
}
