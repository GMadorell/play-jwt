package forms

import com.google.inject.Inject
import models.JwtToken
import play.api.data.Form
import play.api.data.Forms._
import services.jwt.JwtAuthenticator

class JwtTokenForm @Inject()(jwtAuthenticator: JwtAuthenticator) {
  val form = Form(
    mapping(
      "jwtToken" -> text
    )(JwtToken.apply)(JwtToken.unapply) verifying("Wrong username or password", fields => fields match {
      case jwtToken => verifyJwtToken(jwtToken)
    })
  )

  def verifyJwtToken(jwtToken: JwtToken): Boolean = {
    jwtAuthenticator.isValid(jwtToken)
  }
}
