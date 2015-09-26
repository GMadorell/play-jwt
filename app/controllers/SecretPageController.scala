package controllers

import com.google.inject.Inject
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Controller
import services.jwt.JwtAuthenticator
import utils.JwtAuthentication

class SecretPageController @Inject()(implicit val jwtAuthenticator: JwtAuthenticator,
                                     implicit val messagesApi: MessagesApi)
  extends Controller with JwtAuthentication {

  def secret = JwtAuthenticatedAction { user =>
    Ok(Json.obj("hello" -> user.username))
  }
}