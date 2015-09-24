package services.user

import models.User
import org.scalatestplus.play.PlaySpec
import services.user.exception.{UserNotExistsException, UserAlreadyExistsException}

class UserDAOInMemoryTest extends PlaySpec {
  val user = User("username", "password")

  "UserDAOInMemory" should {
    "allow retrieval of an added user" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      val foundUserOption = userDao.retrieve(user.username, user.password)
      foundUserOption.get mustEqual user
    }
    "not retrieve a user that hasn't been added" in {
      val userDao = new UserDAOInMemory
      val foundUserOption = userDao.retrieve(user.username, user.password)
      foundUserOption mustEqual None
    }
    "raise exception when trying to add a user with the same username as an already added user" in {
      val userDao = new UserDAOInMemory
      val secondUser = User("username", "password")
      userDao.add(user)
      intercept[UserAlreadyExistsException] {
        userDao.add(secondUser)
      }
    }
    "acknowledge that a not added user doesn't exist" in {
      val userDao = new UserDAOInMemory
      userDao.exists(user.username) mustBe false
    }
    "acknowledge that an added user exists" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      userDao.exists(user.username) mustBe true
    }
    "remove a user" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      userDao.remove(user)
      userDao.exists(user.username) mustBe false
    }
    "raise exception when trying to remove an nonexistent user" in {
      val userDao = new UserDAOInMemory
      intercept[UserNotExistsException] {
        userDao.remove(user)
      }
    }
  }
}
