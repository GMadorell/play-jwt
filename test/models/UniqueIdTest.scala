package models

import org.scalatestplus.play.PlaySpec

class UniqueIdTest extends PlaySpec {
  "UniqueId" should {
    "generate valid uuids" in {
      val uniqueId = UniqueIdGenerator.generate
      UniqueIdValidator.isValid(uniqueId.uuid)
    }
    "not allow empty string" in {
      intercept[IllegalArgumentException] {
        UniqueId("")
      }
    }
    "not allow a zero appended uuid" in {
      intercept[IllegalArgumentException] {
        UniqueId("00000000000000000000000000000009bf989f-5b24-47bc-871e-1e824d4f4c60")
      }
    }
    "not allow uuids without hyphens" in {
      intercept[IllegalArgumentException] {
        UniqueId("41bb214ee51f4e41b8c156198d29bb1d")
      }
    }
    "allow valid uuids" in {
      UniqueId("09bf989f-5b24-47bc-871e-1e824d4f4c60")
      UniqueId("41bb214e-e51f-4e41-b8c1-56198d29bb1d")
      UniqueId("a7495449-5902-4b59-9c40-6965695f3668")
    }
  }

}
