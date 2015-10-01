package forms

import com.google.inject.Inject
import models.LoginInfo
import play.api.data.Forms._
import play.api.data._
import services.user.UserDAO


class SignUpForm @Inject()(userDAO: UserDAO) {
  val form = Form(
    mapping(
      "username" -> text,
      "password" -> text
    )(LoginInfo.apply)(LoginInfo.unapply) verifying("User already exists", fields => fields match {
      case loginInfo => !userDAO.existsByName(loginInfo.username)
    })
  )
}
