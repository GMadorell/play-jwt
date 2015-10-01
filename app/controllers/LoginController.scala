package controllers

import com.google.inject.Inject
import forms.LoginForm
import models.LoginInfo
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.jwt.authenticator.JwtAuthenticator
import services.user.UserDAO

class LoginController @Inject()(val loginForm: LoginForm,
                                val jwtAuthenticator: JwtAuthenticator,
                                val userDAO: UserDAO,
                                implicit val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  def login() = Action { implicit request =>
    loginForm.form.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errorsAsJson),
      loginInfo => handleCorrectLogin(loginInfo)
    )
  }

  def handleCorrectLogin(loginInfo: LoginInfo): Result = {
    val user = userDAO.retrieveByName(loginInfo.username).get
    val jwtToken = jwtAuthenticator.generateToken(user)
    Ok(Json.toJson(jwtToken))
  }
}
