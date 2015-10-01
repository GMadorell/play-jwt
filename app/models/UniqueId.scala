package models

import java.util.UUID

case class UniqueId(uuid: String) {
  require(UniqueIdValidator.isValid(uuid), s"The given uuid is not valid: $uuid")
}

object UniqueIdValidator {
  val uuidReg = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$".r

  def isValid(uuid: String): Boolean = uuidReg.findFirstIn(uuid).isDefined
}

object UniqueIdGenerator {
  def generate = UniqueId(UUID.randomUUID().toString)
}
