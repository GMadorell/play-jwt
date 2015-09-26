package forms

import com.google.inject.Inject
import models.LoginInfo
import play.api.data.Forms._
import play.api.data._
import services.user.UserDAO

class LoginForm @Inject()(userDAO: UserDAO) {
  val form = Form(
    mapping(
      "username" -> text,
      "password" -> text
    )(LoginInfo.apply)(LoginInfo.unapply) verifying("Wrong username or password", fields => fields match {
      case loginInfo => verifyLoginInfo(loginInfo)
    })
  )

  def verifyLoginInfo(loginInfo: LoginInfo): Boolean = {
    false
  }
}

