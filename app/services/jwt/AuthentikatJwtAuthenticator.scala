package services.jwt

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.inject.Inject
import models.{JwtToken, User}
import org.json4s.DefaultFormats
import play.api.Play
import services.jwt.exception.InvalidJwtTokenException
import services.user.UserDAO
import utils.Time

import scala.util.Try

class AuthentikatJwtAuthenticator @Inject()(userDAO: UserDAO)
  extends JwtAuthenticator {

  private val Secret = Play.current.configuration.getString("auth.secret").get
  private val Algorithm = Play.current.configuration.getString("auth.algorithm").get
  private val TokenDurationMilliseconds: Long =
    Play.current.configuration.getLong("auth.tokendurationmillis").getOrElse(-1)

  private val header = JwtHeader(Algorithm)
  implicit val formats = DefaultFormats

  override def authenticateUser(user: User): JwtToken = {
    val claimsSet = JwtClaimsSet(Map(
      "username" -> user.username,
      "issuedAt" -> Time.getUnixTimestampMilliseconds)
    )
    val token = JsonWebToken(header, claimsSet, Secret)
    JwtToken(token)
  }

  override def getUserFromToken(jwtToken: JwtToken): Try[Option[User]] = Try {
    println(TokenDurationMilliseconds)
    isValid(jwtToken) match {
      case false => throw InvalidJwtTokenException("Invalid JWT Token")
      case true =>
        val claims = jwtToken.token match {
          case JsonWebToken(_, claimsSet, _) => claimsSet.jvalue
          case _ => throw InvalidJwtTokenException("The given string doesn't seem to be a jwtToken")
        }
        (claims \ "issuedAt").extractOpt[Long] match {
          case None => throw InvalidJwtTokenException("JWT token didn't have a 'issuedAt' field in it")
          case Some(issuedAt) => guardIssuedAt(issuedAt)
        }
        (claims \ "username").extractOpt[String] match {
          case None => throw InvalidJwtTokenException("JWT token didn't have a username in it")
          case Some(username) => userDAO.retrieve(username)
        }
    }
  }

  def guardIssuedAt(issuedAt: Long) = {
    if (TokenDurationMilliseconds > 0L && issuedAt + TokenDurationMilliseconds < Time.getUnixTimestampMilliseconds)
      throw InvalidJwtTokenException("JWT token timed out")
  }

  override def isValid(jwtToken: JwtToken): Boolean = JsonWebToken.validate(jwtToken.token, Secret)
}
