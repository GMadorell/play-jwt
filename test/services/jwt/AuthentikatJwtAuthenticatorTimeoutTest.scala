package services.jwt

import models.User
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test.FakeApplication
import services.jwt.exception.InvalidJwtTokenException
import services.user.UserDAO

import scala.util.{Failure, Success}

class AuthentikatJwtAuthenticatorTimeoutTest extends PlaySpec with MockitoSugar with OneAppPerSuite {

  implicit override lazy val app: FakeApplication =
    FakeApplication(
      additionalConfiguration = Map("auth.tokendurationmillis" -> "1000")
    )

  val user = User("username", "password")

  "AuthentikatJwtAuthenticator" should {
    "throw InvalidJwtTokenException when the token times out" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      Thread sleep 1001
      authenticator.guardIsValid(token) match {
        case Success(unit) => fail("Authenticator should raise InvalidJwtTokenException on an timed out token")
        case Failure(ex) => ex mustBe an[InvalidJwtTokenException]
      }
    }
    "acknowledge as valid a token that didn't time out" in {
      val userDAO = mock[UserDAO]
      val authenticator = new AuthentikatJwtAuthenticator(userDAO)
      val token = authenticator.authenticateUser(user)
      authenticator.guardIsValid(token) match {
        case Success(unit) => Unit  // All ok
        case Failure(ex) => fail("Authenticator shouldn't raise an exception on a valid token", ex)
      }
    }
  }
}
