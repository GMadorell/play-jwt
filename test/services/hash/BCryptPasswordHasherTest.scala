package services.hash

import org.scalatestplus.play.PlaySpec

class BCryptPasswordHasherTest extends PlaySpec {
  val passwordHasher = new BCryptPasswordHasher

  "BCryptPasswordHasher" should {
    "check that an encrypted password matches the unencrypted version" in {
      val password = "somepassword"
      val hashed = passwordHasher.hash(password)
      passwordHasher.check(hashed, password) mustBe true
    }
    "not match hashes of two different passwords" in {
      val firstPassword = "somepassword"
      val secondPassword = "anotherpassword"
      val firstHash = passwordHasher.hash(firstPassword)
      val secondHash = passwordHasher.hash(secondPassword)
      passwordHasher.check(firstHash, secondPassword) mustBe false
      passwordHasher.check(secondHash, firstPassword) mustBe false
    }
  }
}
