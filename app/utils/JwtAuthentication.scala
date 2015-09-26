package utils

import forms.JwtTokenForm
import models.{JwtToken, User}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.jwt.JwtAuthenticator

import scala.util.{Failure, Success}

trait JwtAuthentication extends I18nSupport {
  self: Controller =>

  object JwtAuthenticatedAction {
    def apply(f: User => Result)
             (implicit jwtAuthenticator: JwtAuthenticator) = Action { implicit request =>
      val jwtTokenForm = new JwtTokenForm(jwtAuthenticator)
      jwtTokenForm.form.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.errorsAsJson),
        jwtToken => handleCorrectJwtToken(jwtToken, jwtAuthenticator, f)
      )
    }

    def handleCorrectJwtToken(jwtToken: JwtToken, jwtAuthenticator: JwtAuthenticator, f: User => Result): Result = {
      jwtAuthenticator.getUserFromToken(jwtToken) match {
        case Failure(ex) => Forbidden(ex.getMessage)
        case Success(userOption) => userOption match {
          case None => NotFound(Json.obj("error" -> "User not found"))
          case Some(user) => f(user)
        }
      }
    }
  }

}

