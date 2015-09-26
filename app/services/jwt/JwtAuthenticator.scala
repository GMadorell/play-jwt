package services.jwt

import models.{JwtToken, User}

import scala.util.Try

trait JwtAuthenticator {
  /**
   * @return The JWT that holds that authenticates the user.
   */
  def authenticateUser(user: User): JwtToken

  def isValid(jwtToken: JwtToken): Boolean

  def getUserFromToken(jwtToken: JwtToken): Try[Option[User]]
}
