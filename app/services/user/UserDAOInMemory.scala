package services.user

import javax.inject._

import models.{UniqueId, User}
import services.user.exception.{UserAlreadyExistsException, UserNotExistsException}

@Singleton // Dependency injection. This lets guice know that only one element of this class shall be created.
class UserDAOInMemory extends UserDAO {
  private var userMap = Map[UniqueId, User]()

  override def add(user: User): Unit = {
    if (existsByName(user.username)) throw UserAlreadyExistsException(s"User with name '${user.username}' already exists")
    if (exists(user.uuid)) throw UserAlreadyExistsException(s"User with uuid '${user.uuid}' already exists exist")
    else userMap += (user.uuid -> user)
  }

  override def remove(user: User): Unit = {
    if (!exists(user.uuid)) throw UserNotExistsException(s"User with uuid '${user.uuid}' doesn't exist")
    else userMap -= user.uuid
  }

  override def exists(userId: UniqueId): Boolean = retrieve(userId).isDefined

  override def existsByName(username: String): Boolean = retrieveByName(username).isDefined

  override def retrieve(userId: UniqueId): Option[User] = userMap.get(userId)

  override def retrieveByName(username: String): Option[User] = {
    userMap.values.find(u => u.username == username)
  }
}
