package utils

import forms.JwtTokenForm
import models.token.JwtToken
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, Controller, Result}
import services.jwt.JwtAuthenticator

import scala.util.{Failure, Success}

trait JwtAuthentication extends I18nSupport {
  self: Controller =>

  object JwtAuthenticatedAction {
    def apply(f: JwtToken => Result)
             (implicit jwtAuthenticator: JwtAuthenticator) = Action { implicit request =>
      val jwtTokenForm = new JwtTokenForm(jwtAuthenticator)
      jwtTokenForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.errorsAsJson),
        jwtToken => handleCorrectJwtToken(jwtToken, jwtAuthenticator, f)
      )
    }

    def handleCorrectJwtToken(jwtToken: JwtToken, jwtAuthenticator: JwtAuthenticator, f: JwtToken => Result): Result = {
      jwtAuthenticator.guardIsValid(jwtToken) match {
        case Failure(ex) => Forbidden(ex.getMessage)
        case Success(unit) => f(jwtToken)
      }
    }
  }

}

