package services.user.exception

case class UserNotExistsException(message: String) extends Exception(message)
