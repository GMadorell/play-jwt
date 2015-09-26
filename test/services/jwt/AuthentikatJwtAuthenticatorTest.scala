package services.jwt

import models.{JwtToken, User}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import services.jwt.exception.InvalidJwtTokenException
import services.user.UserDAO

import scala.util.{Failure, Success}

class AuthentikatJwtAuthenticatorTest extends PlaySpec with MockitoSugar {

  val user = User("username", "password")

  "AuthentikatJwtAuthenticator" should {
    "mark as valid a token created by itself" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      authenticator.isValid(token) == true
    }
    "mark as invalid a non jwt string" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      authenticator.isValid(JwtToken("some string that isn't a jwt")) == false
    }
    "mark as invalid a modified token" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val jwtToken = authenticator.authenticateUser(user)
      val modifiedToken = JwtToken(jwtToken.token.dropRight(1))
      authenticator.isValid(modifiedToken) == false
    }
    "get a user from a valid token" in {
      val userDAO = mock[UserDAO]
      when(userDAO.retrieve(user.username)) thenReturn Some(user.copy())
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      val userFromTokenTry = authenticator.getUserFromToken(token)
      userFromTokenTry match {
        case Success(userOption) => userOption.get == user
        case Failure(ex) => fail("Authenticator threw an exception when it shouldn't", ex)
      }
    }
    "throw InvalidJwtTokenException when trying to get a user from an invalid token" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val invalidToken = JwtToken("some random string that is not a valid token")
      authenticator.getUserFromToken(invalidToken) match {
        case Success(userOption) => fail("Authenticator shouldn't return a user from an invalid token")
        case Failure(ex) => ex mustBe an[InvalidJwtTokenException]
      }
    }
  }
}
