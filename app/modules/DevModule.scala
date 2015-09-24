package modules

import com.google.inject.{AbstractModule, Provides}
import forms.LoginForm
import net.codingwell.scalaguice.ScalaModule
import services.user.{UserDAO, UserDAOInMemory}

class DevModule extends AbstractModule with ScalaModule {
  def configure() = {
    bind[UserDAO].to[UserDAOInMemory]
  }

  @Provides
  def provideLoginForm(userDAO: UserDAO) = new LoginForm(userDAO)
}
