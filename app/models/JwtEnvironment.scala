package models

import services.jwt.authenticator.JwtAuthenticator
import services.jwt.blacklist.JwtBlacklist

case class JwtEnvironment(authenticator: JwtAuthenticator,
                          blacklist: JwtBlacklist)
