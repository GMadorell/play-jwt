package utils

object Time {
  def getUnixTimestampSeconds = System.currentTimeMillis() / 1000L
  def getUnixTimestampMilliseconds = System.currentTimeMillis()
}
