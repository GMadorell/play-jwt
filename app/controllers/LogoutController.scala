package controllers

import com.google.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc.Controller
import services.jwt.JwtAuthenticator
import utils.JwtAuthentication

class LogoutController @Inject()(implicit val messagesApi: MessagesApi,
                                 implicit val jwtAuthenticator: JwtAuthenticator)
  extends Controller with JwtAuthentication {

  def logout = JwtAuthenticatedAction { user =>
    NotImplemented
  }
}
