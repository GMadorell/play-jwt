import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  def postJson(uri: String, body: JsValue) = new FakeRequest(POST, uri, FakeHeaders(), body)

  "Application" should {
    "send OK on a signup request" in new WithApplication {
      val signupRequest = postJson("/signup", Json.obj("username" -> "test", "password" -> "test"))
      val Some(result) = route(signupRequest)

      status(result) must equalTo(CREATED)
    }
  }
}
