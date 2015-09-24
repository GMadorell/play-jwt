package controllers

import com.google.inject.Inject
import forms.LoginForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(val loginForm: LoginForm,
                                implicit val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  def login() = Action { implicit request =>
    loginForm.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(formWithErrors.errorsAsJson)
      },
      loginInfo => {
        Ok("Good")
      }
    )
  }
}
