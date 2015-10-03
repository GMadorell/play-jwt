package services.jwt.blacklist

import com.google.inject.Singleton
import models.UniqueId
import models.token.JwtToken

@Singleton
class JwtBlacklistInMemory extends JwtBlacklist {
  var uniqueIdSet = Set[UniqueId]()

  override def add(jwtToken: JwtToken): Unit = uniqueIdSet = uniqueIdSet + jwtToken.tokenId

  override def remove(jwtToken: JwtToken): Unit = uniqueIdSet = uniqueIdSet - jwtToken.tokenId

  override def isTokenBlacklisted(jwtToken: JwtToken): Boolean = uniqueIdSet.contains(jwtToken.tokenId)
}
