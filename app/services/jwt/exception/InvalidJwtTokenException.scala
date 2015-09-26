package services.jwt.exception

case class InvalidJwtTokenException(message: String) extends Exception(message)
