package models.token

import models.UniqueId
import play.api.libs.json._


case class JwtToken(issuedAt: Long, tokenId: UniqueId, userId: UniqueId, token: String)

object JwtToken {
  implicit val jwtTokenWrites = new Writes[JwtToken] {
    override def writes(jwtToken: JwtToken): JsValue = {
      Json.obj("jwtToken" -> jwtToken.token)
    }
  }
}


