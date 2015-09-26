package services.jwt

import models.User

import scala.util.Try

trait JwtAuthenticator {
  /**
   * @return The JWT that holds that authenticates the user.
   */
  def authenticateUser(user: User): String

  def isValid(jwtToken: String): Boolean

  def getUserFromToken(jwtToken: String): Try[Option[User]]
}
