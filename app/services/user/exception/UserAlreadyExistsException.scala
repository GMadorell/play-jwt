package services.user.exception

case class UserAlreadyExistsException(message: String) extends Exception(message)
