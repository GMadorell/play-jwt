import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class FunctionalTest extends Specification {

  val username = "username"
  val password = "password"

  "Application" should {
    "send OK on a signup request" in new WithApplication {
      signUp(username, password)
    }
    "not allow login if a user that doesn't exist" in new WithApplication {
      login(username, password, BAD_REQUEST)
    }
    "allow login of a signed up user" in new WithApplication {
      signUp(username, password)
      login(username, password)
    }
    "login should return a jwtToken" in new WithApplication {
      signUp(username, password)
      val loginJson = login(username, password)
      (loginJson \ "jwtToken").as[String]
    }
    "not allow accessing the secret page without a jwtToken" in new WithApplication {
      val req = post("/secret")
      val Some(result) = route(req)
      status(result) must equalTo(BAD_REQUEST)
    }
    "not allow accessing the secret page with an invalid jwtToken" in new WithApplication {
      val someRandomToken = "some.random.token"
      secretPage(someRandomToken, FORBIDDEN)
    }
    "allow accessing the secret page with a valid jwtToken" in new WithApplication {
      signUp(username, password)
      val loginJson = login(username, password)
      val jwtToken = (loginJson \ "jwtToken").as[String]
      secretPage(jwtToken)
    }
    "not allow accessing the logout page without a jwtToken" in new WithApplication {
      val req = post("/logout")
      val Some(result) = route(req)
      status(result) must equalTo(BAD_REQUEST)
    }
    "not allow accessing the logout page with an invalid jwtToken" in new WithApplication {
      val someRandomToken = "some.random.token"
      logout(someRandomToken, FORBIDDEN)
    }
    "allow accessing the logout page with a valid jwtToken" in new WithApplication {
      signUp(username, password)
      val loginJson = login(username, password)
      val jwtToken = (loginJson \ "jwtToken").as[String]
      logout(jwtToken)
    }
    "not allow accessing the logout page twice with the same jwtToken" in new WithApplication {
      signUp(username, password)
      val loginJson = login(username, password)
      val jwtToken = (loginJson \ "jwtToken").as[String]
      logout(jwtToken)
      logout(jwtToken, expectedStatus = FORBIDDEN)
    }
    "not allow accessing the secret page after logging out" in new WithApplication {
      signUp(username, password)
      val loginJson = login(username, password)
      val jwtToken = (loginJson \ "jwtToken").as[String]
      secretPage(jwtToken)
      logout(jwtToken)
      secretPage(jwtToken, expectedStatus = FORBIDDEN)
    }
  }

  def signUp(username: String, password: String, expectedStatus: Int = CREATED) = {
    val signUpRequest = postJson("/signup", Json.obj("username" -> username, "password" -> password))
    val Some(result) = route(signUpRequest)
    status(result) must equalTo(expectedStatus)
  }

  def login(username: String, password: String, expectedStatus: Int = OK) = {
    val loginRequest = postJson("/login", Json.obj("username" -> username, "password" -> password))
    val Some(result) = route(loginRequest)
    status(result) must equalTo(expectedStatus)
    jsonResult(result)
  }

  def secretPage(jwtToken: String, expectedStatus: Int = OK) = {
    val secretPageRequest = postJson("/secret", Json.obj("jwtToken" -> jwtToken))
    val Some(result) = route(secretPageRequest)
    status(result) must equalTo(expectedStatus)
    jsonResult(result)
  }

  def logout(jwtToken: String, expectedStatus: Int = ACCEPTED) = {
    val secretPageRequest = postJson("/logout", Json.obj("jwtToken" -> jwtToken))
    val Some(result) = route(secretPageRequest)
    status(result) must equalTo(expectedStatus)
  }

  def postJson(uri: String, body: JsValue) = FakeRequest(POST, uri, FakeHeaders(), body)

  def post(uri: String) = FakeRequest(POST, uri)

  def jsonResult(result: Future[mvc.Result]): JsValue = {
    contentType(result) must beSome("application/json")
    Json.parse(contentAsString(result))
  }
}
