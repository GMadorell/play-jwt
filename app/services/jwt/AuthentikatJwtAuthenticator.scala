package services.jwt

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.inject.Inject
import models.{JwtToken, User}
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


  override def authenticateUser(user: User): JwtToken = {
    val claimsSet = JwtClaimsSet(Map("username" -> user.username))
    val token = JsonWebToken(header, claimsSet, Secret)
    JwtToken(token)
  }

  override def getUserFromToken(jwtToken: JwtToken): Try[Option[User]] = Try {
    isValid(jwtToken) match {
      case false => throw InvalidJwtTokenException("Invalid JWT Token")
      case true =>
        val claims = jwtToken.token match {
          case JsonWebToken(_, claimsSet, _) => claimsSet.jvalue
          case _ => throw InvalidJwtTokenException("The given string doesn't seem to be a jwtToken")
        }
        (claims \ "username").extractOpt[String] match {
          case None => throw InvalidJwtTokenException("JWT token didn't have a username in it")
          case Some(username) => userDAO.retrieve(username)
        }
    }
  }

  override def isValid(jwtToken: JwtToken): Boolean = JsonWebToken.validate(jwtToken.token, Secret)
}
