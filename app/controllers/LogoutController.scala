package controllers

import com.google.inject.Inject
import models.JwtEnvironment
import play.api.i18n.MessagesApi
import play.api.mvc.Controller
import utils.JwtAuthentication

class LogoutController @Inject()(implicit val messagesApi: MessagesApi,
                                 implicit val jwtEnvironment: JwtEnvironment)
  extends Controller with JwtAuthentication {

  def logout = JwtAuthenticatedAction { jwtToken =>
    jwtEnvironment.blacklist.add(jwtToken)
    Accepted
  }
}
