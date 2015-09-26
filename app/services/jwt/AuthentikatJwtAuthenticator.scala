package services.jwt

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.inject.Inject
import models.User
import org.json4s.DefaultFormats
import services.jwt.exception.InvalidJwtTokenException
import services.user.UserDAO

import scala.util.Try

class AuthentikatJwtAuthenticator @Inject()(userDAO: UserDAO)
  extends JwtAuthenticator {

  // #TODO move these to config parameters
  private val Secret = "thisShouldBeChangedBecauseItsNotSecret"
  private val Algorithm = "HS256"

  private val header = JwtHeader(Algorithm)
  implicit val formats = DefaultFormats


  override def authenticateUser(user: User): String = {
    val claimsSet = JwtClaimsSet(Map("username" -> user.username))
    JsonWebToken(header, claimsSet, Secret)
  }

  override def getUserFromToken(jwtToken: String): Try[Option[User]] = Try {
    isValid(jwtToken) match {
      case false => throw InvalidJwtTokenException("Invalid JWT Token")
      case true =>
        val claims = jwtToken match {
          case JsonWebToken(_, claimsSet, _) => claimsSet.jvalue
          case _ => throw InvalidJwtTokenException("The given string doesn't seem to be a jwtToken")
        }
        (claims \ "username").extractOpt[String] match {
          case None => throw InvalidJwtTokenException("JWT token didn't have a username in it")
          case Some(username) => userDAO.retrieve(username)
        }
    }
  }

  override def isValid(jwtToken: String): Boolean = JsonWebToken.validate(jwtToken, Secret)
}
