package services.user

trait PasswordInfoDAO {
  def exists(username: String, password: String): Boolean
}
