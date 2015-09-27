package services.jwt

import models.{JwtToken, User}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.jwt.exception.InvalidJwtTokenException
import services.user.UserDAO

import scala.util.{Failure, Success}

class AuthentikatJwtAuthenticatorTest extends PlaySpec with MockitoSugar with OneAppPerSuite {

  val user = User("username", "password")

  "AuthentikatJwtAuthenticator" should {
    "mark as valid a token created by itself" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      authenticator.guardIsValid(token).isSuccess mustBe true
    }
    "mark as invalid a non jwt string" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      authenticator.guardIsValid(JwtToken("some string that isn't a jwt")).isFailure mustBe true
    }
    "mark as invalid a modified token" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val jwtToken = authenticator.authenticateUser(user)
      val modifiedToken = JwtToken(jwtToken.token.dropRight(1))
      authenticator.guardIsValid(modifiedToken).isFailure mustBe true
    }
    "get a user from a valid token" in {
      val userDAO = mock[UserDAO]
      when(userDAO.retrieve(user.username)) thenReturn Some(user.copy())
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      authenticator.getUserFromToken(token).get equals user
    }
    "throw InvalidJwtTokenException when trying to get a user from an invalid token" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val invalidToken = JwtToken("some random string that is not a valid token")
      intercept[InvalidJwtTokenException] {
        authenticator.getUserFromToken(invalidToken)
      }
    }
  }
}
