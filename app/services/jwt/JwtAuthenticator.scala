package services.jwt

import models.{JwtToken, User}

import scala.util.Try

trait JwtAuthenticator {
  /**
   * @return The JWT that holds that authenticates the user.
   */
  def authenticateUser(user: User): JwtToken

  /**
   * @return a try that will be filled with an exception on failure
   *         and empty on success (success means that the token is valid)
   */
  def guardIsValid(jwtToken: JwtToken): Try[Unit]

  def getUserFromToken(jwtToken: JwtToken): Option[User]
}
