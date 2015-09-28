package services.jwt.authenticator

import exception.InvalidJwtTokenException
import models.User
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Configuration

import scala.util.{Failure, Success}

class AuthentikatJwtAuthenticatorTimeoutTest extends PlaySpec with MockitoSugar {

  val user = User("username", "password")

  def getConfigurationMockWithTimeout(tokenTimeoutMilliseconds: Long) = {
    val configuration = mock[Configuration]
    when(configuration.getString("auth.secret")) thenReturn Some("not really a secret")
    when(configuration.getString("auth.algorithm")) thenReturn Some("HS256")
    when(configuration.getLong("auth.tokendurationmillis")) thenReturn Some(tokenTimeoutMilliseconds)
    configuration
  }

  "AuthentikatJwtAuthenticator" should {
    "throw InvalidJwtTokenException when the token times out" in {
      val configuration = getConfigurationMockWithTimeout(500)
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val token = authenticator.generateToken(user)
      Thread sleep 501
      authenticator.guardIsValid(token) match {
        case Success(unit) => fail("Authenticator should raise InvalidJwtTokenException on an timed out token")
        case Failure(ex) => ex mustBe an[InvalidJwtTokenException]
      }
    }
    "acknowledge as valid a token that didn't time out" in {
      val configuration = getConfigurationMockWithTimeout(500)
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val token = authenticator.generateToken(user)
      authenticator.guardIsValid(token) match {
        case Success(unit) => Unit // All ok
        case Failure(ex) => fail("Authenticator shouldn't raise an exception on a valid token", ex)
      }
    }
  }
}
