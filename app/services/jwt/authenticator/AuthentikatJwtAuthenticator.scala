package services.jwt.authenticator

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.inject.Inject
import models._
import models.token.JwtToken
import org.json4s.DefaultFormats
import play.api.Configuration
import exception.InvalidJwtTokenException
import utils.Time

import scala.util.Try

class AuthentikatJwtAuthenticator @Inject()(configuration: Configuration)
  extends JwtAuthenticator {

  private val Secret = configuration.getString("auth.secret").get
  private val TokenDurationMilliseconds: Long = configuration.getLong("auth.tokendurationmillis").getOrElse(-1)
  private val header = JwtHeader(configuration.getString("auth.algorithm").get)

  private implicit val json4sFormats = DefaultFormats

  override def generateToken(user: User): JwtToken = {
    val issuedAt = Time.getUnixTimestampMilliseconds
    val userId = user.uuid
    val tokenId = UniqueIdGenerator.generate
    val claimsSet = JwtClaimsSet(Map(
      "userId" -> userId,
      "issuedAt" -> issuedAt,
      "tokenId" -> tokenId
    ))
    val token = JsonWebToken(header, claimsSet, Secret)
    JwtToken(issuedAt, tokenId, userId, token)
  }

  override def fromString(token: String): JwtToken = {
    val claims = token match {
      case JsonWebToken(_, claimsSet, _) => claimsSet.jvalue
      case _ => throw InvalidJwtTokenException("The given string doesn't seem to be a jwtToken")
    }
    val issuedAt = (claims \ "issuedAt").extractOrElse[Long](
      throw InvalidJwtTokenException("JWT token didn't have a 'issuedAt' field in it")
    )
    val tokenId = (claims \ "tokenId").extractOrElse[String](
      throw InvalidJwtTokenException("JWT token didn't have a 'tokenId' field in it")
    )
    val userId = (claims \ "userId").extractOrElse[String](
      throw InvalidJwtTokenException("JWT token didn't have a userId in it")
    )

    new JwtToken(issuedAt, UniqueId(tokenId), UniqueId(userId), token)
  }

  override def guardIsValid(jwtToken: JwtToken): Try[Unit] = Try {
    JsonWebToken.validate(jwtToken.token, Secret) match {
      case false => throw InvalidJwtTokenException("Invalid JWT Token")
      case true => guardIssuedAt(jwtToken.issuedAt)
    }
  }

  private def guardIssuedAt(issuedAt: Long) = {
    if (TokenDurationMilliseconds > 0L && issuedAt + TokenDurationMilliseconds < Time.getUnixTimestampMilliseconds)
      throw InvalidJwtTokenException("JWT token timed out")
  }
}
