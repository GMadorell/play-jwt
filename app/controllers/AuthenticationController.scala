package controllers

import com.google.inject.Inject
import forms.LoginInfoForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.user.PasswordInfoDAO

class AuthenticationController @Inject()(val passwordInfoDAO: PasswordInfoDAO,
                                         val loginInfoForm: LoginInfoForm,
                                         implicit val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  def authenticate() = Action { implicit request =>
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
