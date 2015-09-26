package models

import play.api.libs.json._



case class JwtToken(token: String)

object JwtToken {
  implicit val jwtTokenWrites = new Writes[JwtToken] {
    override def writes(jwtToken: JwtToken): JsValue = {
      Json.obj("jwtToken" -> jwtToken.token)
    }
  }
}


