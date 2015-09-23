package forms

import com.google.inject.Inject
import models.LoginInfo
import play.api.data.Forms._
import play.api.data._
import services.user.PasswordInfoDAO

class LoginInfoForm @Inject() (passwordInfoDAO: PasswordInfoDAO) {
  val form = Form(
    mapping(
      "username" -> text,
      "password" -> text
    )(LoginInfo.apply)(LoginInfo.unapply) verifying("Wrong username or password", fields => fields match {
      case loginInfo => passwordInfoDAO.exists(loginInfo.username, loginInfo.password)
    })
  )
}

