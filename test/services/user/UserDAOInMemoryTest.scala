package services.user

import models.User
import org.scalatestplus.play.PlaySpec
import services.user.exception.{UserNotExistsException, UserAlreadyExistsException}

class UserDAOInMemoryTest extends PlaySpec {
  val user = User("username", "password")

  "UserDAOInMemory" should {
    "allow retrieval of an added user by id" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      val foundUserOption = userDao.retrieve(user.uuid)
      foundUserOption.get mustEqual user
    }
    "allow retrieval of an added user by username" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      val foundUserOption = userDao.retrieveByName(user.username)
      foundUserOption.get mustEqual user
    }
    "not retrieve a user that hasn't been added" in {
      val userDao = new UserDAOInMemory
      val foundUserOption = userDao.retrieve(user.uuid)
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
    "raise exception when trying to add a user with the same uuid as an already added user" in {
      val userDao = new UserDAOInMemory
      val secondUser = User("username", "password", user.uuid)
      userDao.add(user)
      intercept[UserAlreadyExistsException] {
        userDao.add(secondUser)
      }
    }
    "acknowledge that a not added user doesn't exist by id" in {
      val userDao = new UserDAOInMemory
      userDao.exists(user.uuid) mustBe false
    }
    "acknowledge that a not added user doesn't exist by uername" in {
      val userDao = new UserDAOInMemory
      userDao.existsByName(user.username) mustBe false
    }
    "acknowledge that an added user exists by id" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      userDao.exists(user.uuid) mustBe true
    }
    "acknowledge that an added user exists by username" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      userDao.existsByName(user.username) mustBe true
    }
    "remove a user" in {
      val userDao = new UserDAOInMemory
      userDao.add(user)
      userDao.remove(user)
      userDao.exists(user.uuid) mustBe false
    }
    "raise exception when trying to remove an nonexistent user" in {
      val userDao = new UserDAOInMemory
      intercept[UserNotExistsException] {
        userDao.remove(user)
      }
    }
  }
}
