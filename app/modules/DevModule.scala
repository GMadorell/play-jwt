package modules

import com.google.inject.{AbstractModule, Provides}
import forms.{SignUpForm, LoginForm}
import net.codingwell.scalaguice.ScalaModule
import services.jwt.{AuthentikatJwtAuthenticator, JwtAuthenticator}
import services.user.{UserDAO, UserDAOInMemory}

class DevModule extends AbstractModule with ScalaModule {
  def configure() = {
    bind[UserDAO].to[UserDAOInMemory]
    bind[JwtAuthenticator].to[AuthentikatJwtAuthenticator]
  }

  @Provides
  def provideLoginForm(userDAO: UserDAO) = new LoginForm(userDAO)

  @Provides
  def provideSignUpForm(userDAO: UserDAO) = new SignUpForm(userDAO)
}
