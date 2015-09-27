package services.jwt

import models.User
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Configuration
import services.jwt.exception.InvalidJwtTokenException
import utils.Time

class AuthentikatJwtAuthenticatorTest extends PlaySpec with MockitoSugar {

  val configuration = mock[Configuration]
  when(configuration.getString("auth.secret")) thenReturn Some("not really a secret")
  when(configuration.getString("auth.algorithm")) thenReturn Some("HS256")
  when(configuration.getLong("auth.tokendurationmillis")) thenReturn None

  val user = User("username", "password")

  "AuthentikatJwtAuthenticator" should {
    "mark as valid a token created by itself" in {
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val token = authenticator.generateToken(user)
      authenticator.guardIsValid(token).isSuccess mustBe true
    }
    "raise exception when parsing a non jwt string" in {
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      intercept[InvalidJwtTokenException] {
        val token = authenticator.fromString("some string that isn't a jwt")
      }
    }
    "mark as invalid a modified token" in {
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val jwtToken = authenticator.generateToken(user)
      val modifiedToken = jwtToken.copy(token = jwtToken.token.dropRight(1))
      authenticator.guardIsValid(modifiedToken).isFailure mustBe true
    }
    "put the current timestamp in the 'issuedAt' field" in {
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val jwtToken = authenticator.generateToken(user)
      jwtToken.issuedAt mustBe (Time.getUnixTimestampMilliseconds +- 200L)
    }
    "put the correct username in the 'username' field" in {
      val authenticator = new AuthentikatJwtAuthenticator(configuration)
      val firstToken = authenticator.generateToken(user)
      firstToken.username must be(user.username)
      val secondUsername = "xXQuickscopeXx"
      val secondUser = User(secondUsername, "asdfasdf")
      val secondToken = authenticator.generateToken(secondUser)
      secondToken.username must be(secondUser.username)
    }
  }
}
