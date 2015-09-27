package services.hash

import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordHasher extends PasswordHasher {
  override def hash(password: String): String =
    BCrypt.hashpw(password, BCrypt.gensalt())

  override def check(hashedPassword: String, candidate: String): Boolean =
    BCrypt.checkpw(candidate, hashedPassword)
}
