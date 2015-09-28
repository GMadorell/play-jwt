package utils

import forms.JwtTokenForm
import models.JwtEnvironment
import models.token.JwtToken
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, Controller, Result}
import services.jwt.authenticator.JwtAuthenticator

import scala.util.{Failure, Success}

trait JwtAuthentication extends I18nSupport {
  self: Controller =>

  object JwtAuthenticatedAction {
    def apply(f: JwtToken => Result)
             (implicit jwtEnvironment: JwtEnvironment) = Action { implicit request =>
      val jwtTokenForm = new JwtTokenForm(jwtEnvironment.authenticator)
      jwtTokenForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.errorsAsJson),
        jwtToken => handleCorrectJwtToken(jwtToken, jwtEnvironment, f)
      )
    }

    def handleCorrectJwtToken(jwtToken: JwtToken, jwtEnvironment: JwtEnvironment, f: JwtToken => Result): Result = {
      jwtEnvironment.authenticator.guardIsValid(jwtToken) match {
        case Failure(ex) => Forbidden(ex.getMessage)
        case Success(unit) => jwtEnvironment.blacklist.isTokenBlacklisted(jwtToken) match {
          case true => Forbidden("Token is blacklisted (maybe it has already been logged out?)")
          case false => f(jwtToken)
        }
      }
    }
  }

}

