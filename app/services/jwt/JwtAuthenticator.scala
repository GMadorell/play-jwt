package services.jwt

import models.User
import models.token.JwtToken

import scala.util.Try

trait JwtAuthenticator {
  /**
   * @return The JWT that authenticates the user.
   */
  def generateToken(user: User): JwtToken

  def fromString(token: String): JwtToken

  /**
   * @return a try that will be filled with an exception on failure
   *         and empty on success (success means that the token is valid)
   */
  def guardIsValid(jwtToken: JwtToken): Try[Unit]
}
