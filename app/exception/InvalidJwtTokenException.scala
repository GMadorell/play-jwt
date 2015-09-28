package exception

case class InvalidJwtTokenException(message: String) extends Exception(message)
