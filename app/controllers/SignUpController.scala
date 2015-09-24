package controllers

import com.google.inject.Inject
import forms.LoginForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class SignUpController @Inject()(val loginInfoForm: LoginForm,
                                 implicit val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  def signUp() = Action { implicit request =>
    loginInfoForm.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(formWithErrors.errorsAsJson)
      },
      loginInfo => {
        Ok("Good")
      }
    )
  }
}
