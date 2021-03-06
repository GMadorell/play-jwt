package controllers

import com.google.inject.Inject
import forms.SignUpForm
import models.{LoginInfo, User}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller, Result}
import services.hash.PasswordHasher
import services.user.UserDAO

class SignUpController @Inject()(val signUpForm: SignUpForm,
                                 val userDAO: UserDAO,
                                 val passwordHasher: PasswordHasher,
                                 implicit val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  def signUp() = Action { implicit request =>
    signUpForm.form.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errorsAsJson),
      loginInfo => handleCorrectSignUp(loginInfo)
    )
  }

  def handleCorrectSignUp(loginInfo: LoginInfo): Result = {
    val user: User = createNewUser(loginInfo)
    userDAO.add(user)
    Created
  }

  def createNewUser(loginInfo: LoginInfo): User = {
    User(loginInfo.username, passwordHasher.hash(loginInfo.password))
  }
}
