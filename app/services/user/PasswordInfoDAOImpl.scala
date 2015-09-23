package services.user

class PasswordInfoDAOImpl extends PasswordInfoDAO {
  override def exists(username: String, password: String): Boolean = {
    username == "test" && password == "test"
  }
}
