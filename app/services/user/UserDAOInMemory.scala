package services.user

import javax.inject._

import models.User
import services.user.exception.{UserNotExistsException, UserAlreadyExistsException}

@Singleton // Dependency injection. This lets guice know that only one element of this class shall be created.
class UserDAOInMemory extends UserDAO {
  private var userMap = Map[String, User]()

  override def add(user: User): Unit = {
    if (exists(user.username)) throw UserAlreadyExistsException(s"User with name '${user.username}' already exists")
    else userMap += (user.username -> user)
  }

  override def remove(user: User): Unit = {
    if (!exists(user.username)) throw UserNotExistsException(s"User with name '${user.username}' doesn't exist")
    else userMap -= user.username
  }

  override def exists(username: String): Boolean = userMap.keySet.contains(username)

  override def retrieve(username: String): Option[User] = userMap.get(username)
}
