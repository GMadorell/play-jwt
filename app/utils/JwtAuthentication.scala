package utils

import forms.JwtTokenForm
import models.JwtEnvironment
import models.token.JwtToken
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{Request, Action, Controller, Result}
import services.jwt.authenticator.JwtAuthenticator

import scala.util.{Try, Failure, Success}

trait JwtAuthentication extends I18nSupport {
  self: Controller =>

  object JwtAuthenticatedAction {
    def apply(f: JwtToken => Result)
             (implicit jwtEnvironment: JwtEnvironment) = Action { implicit request =>
      tryToHandleJwtForm(f) match {
        case Failure(ex) => Forbidden(Json.obj("error" -> ex.getMessage))
        case Success(result) => result
      }
    }

    def tryToHandleJwtForm(f: (JwtToken) => Result)
                          (implicit request: Request[_], jwtEnvironment: JwtEnvironment): Try[Result] = Try {
      val jwtTokenForm = new JwtTokenForm(jwtEnvironment.authenticator)
      jwtTokenForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.errorsAsJson),
        jwtToken => handleCorrectJwtToken(jwtToken, f)
      )
    }

    def handleCorrectJwtToken(jwtToken: JwtToken, f: JwtToken => Result)
                             (implicit jwtEnvironment: JwtEnvironment): Result = {
      jwtEnvironment.authenticator.guardIsValid(jwtToken).get
      jwtEnvironment.blacklist.isTokenBlacklisted(jwtToken) match {
        case true => Forbidden(Json.obj("error" -> "Token is blacklisted (maybe it has already been logged out?)"))
        case false => f(jwtToken)
      }
    }
  }
}

