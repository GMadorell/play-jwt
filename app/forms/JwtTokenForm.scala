package forms

import com.google.inject.Inject
import models.token.JwtToken
import play.api.data.Form
import play.api.data.Forms._
import services.jwt.JwtAuthenticator

class JwtTokenForm @Inject()(jwtAuthenticator: JwtAuthenticator) {
  val form = Form(
    mapping(
      "jwtToken" -> text
    )(jwtAuthenticator.fromString)(jwtToken => Some(jwtToken.token))
  )
}
