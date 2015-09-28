package services.jwt.blacklist

import models.token.JwtToken

trait JwtBlacklist {
  def add(jwtToken: JwtToken): Unit
  def remove(jwtToken: JwtToken): Unit
  def isTokenBlacklisted(jwtToken: JwtToken): Boolean
}
