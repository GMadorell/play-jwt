package modules

import com.google.inject.{Provides, AbstractModule}
import forms.LoginInfoForm
import net.codingwell.scalaguice.ScalaModule
import services.user.{PasswordInfoDAOImpl, PasswordInfoDAO}

class DevModule extends AbstractModule with ScalaModule {
  def configure() = {
    bind[PasswordInfoDAO].to[PasswordInfoDAOImpl]
  }

  @Provides
  def provideLoginInfoForm(passwordInfoDAO: PasswordInfoDAO) = new LoginInfoForm(passwordInfoDAO)
}
