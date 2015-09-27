package services.hash

trait PasswordHasher {

  def hash(password: String): String

  /**
   * @return Whether the unencrypted candidate password matches the hashed password
   */
  def check(hashedPassword: String, candidate: String): Boolean
}
