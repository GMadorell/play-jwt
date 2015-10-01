package services.jwt.blacklist

import models.UniqueIdGenerator
import models.token.JwtToken
import org.scalatestplus.play.PlaySpec
import utils.Time

class JwtBlacklistInMemoryTest extends PlaySpec {

  val jwtToken = JwtToken(
    Time.getUnixTimestampMilliseconds, UniqueIdGenerator.generate,
    UniqueIdGenerator.generate, "token")

  "JwtTokenBlacklistInMemory" should {
    "not tell that a jwt token not added to the blacklist is invalid" in {
      val blacklist = new JwtBlacklistInMemory()
      blacklist.isTokenBlacklisted(jwtToken) mustBe false
    }
    "tell that a jwt token added to the blacklist is invalid" in {
      val blacklist = new JwtBlacklistInMemory()
      blacklist.add(jwtToken)
      blacklist.isTokenBlacklisted(jwtToken) mustBe true
    }
    "tell that a token added and retrieved is valid" in {
      val blacklist = new JwtBlacklistInMemory()
      blacklist.add(jwtToken)
      blacklist.remove(jwtToken)
      blacklist.isTokenBlacklisted(jwtToken) mustBe false
    }
  }
}
