package modules

import com.google.inject.{AbstractModule, Provides}
import forms.{JwtTokenForm, SignUpForm, LoginForm}
import models.JwtEnvironment
import net.codingwell.scalaguice.ScalaModule
import services.hash.{BCryptPasswordHasher, PasswordHasher}
import services.jwt.authenticator.{JwtAuthenticator, AuthentikatJwtAuthenticator}
import services.jwt.blacklist.{JwtBlacklistInMemory, JwtBlacklist}
import services.user.{UserDAO, UserDAOInMemory}

class DevModule extends AbstractModule with ScalaModule {
  def configure() = {
    bind[UserDAO].to[UserDAOInMemory]
    bind[PasswordHasher].to[BCryptPasswordHasher]
    bind[JwtAuthenticator].to[AuthentikatJwtAuthenticator]
    bind[JwtBlacklist].to[JwtBlacklistInMemory]
  }

  @Provides
  def provideLoginForm(userDAO: UserDAO, passwordHasher: PasswordHasher) =
    new LoginForm(userDAO, passwordHasher)

  @Provides
  def provideSignUpForm(userDAO: UserDAO) = new SignUpForm(userDAO)

  @Provides
  def provideJwtTokenForm(jwtAuthenticator: JwtAuthenticator) = new JwtTokenForm(jwtAuthenticator)

  @Provides
  def provideJwtEnvironment(jwtAuthenticator: JwtAuthenticator, jwtBlacklist: JwtBlacklist) =
    new JwtEnvironment(jwtAuthenticator, jwtBlacklist)
}
