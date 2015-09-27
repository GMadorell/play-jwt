package controllers

import com.google.inject.Inject
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Controller
import services.jwt.JwtAuthenticator
import services.user.UserDAO
import utils.JwtAuthentication

class SecretPageController @Inject()(userDao: UserDAO,
                                     implicit val jwtAuthenticator: JwtAuthenticator,
                                     implicit val messagesApi: MessagesApi)
  extends Controller with JwtAuthentication {

  def secret = JwtAuthenticatedAction { jwtToken =>
    userDao.retrieve(jwtToken.username) match {
      case None => NotFound(Json.obj("error" -> "User not found"))
      case Some(user) => Ok(Json.obj("hello" -> user.username))
    }

  }
}
