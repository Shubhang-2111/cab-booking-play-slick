package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future

class LoginControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "LoginController" should {

    "render the login form" in {
      val controller = inject[LoginController]
      val request = FakeRequest(GET, "/login")

      val result: Future[Result] = controller.showLoginForm.apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Login")
    }

    "authenticate user with valid credentials" in {
      val controller = inject[LoginController]
      val request = FakeRequest(POST, "/login").withFormUrlEncodedBody(
        "email" -> "test@example.com",
        "password" -> "password"
      )

      val result: Future[Result] = controller.login.apply(request)

      status(result) mustBe OK
      redirectLocation(result) mustBe Some("/booking")
      contentAsString(result) must include("")
    }

    "reject authentication with invalid credentials" in {
      val controller = inject[LoginController]
      val request = FakeRequest(POST, "/login").withFormUrlEncodedBody(
        "email" -> "invalid@example.com",
        "password" -> "wrongpassword"
      )

      val result: Future[Result] = controller.login.apply(request)

      status(result) mustBe UNAUTHORIZED
      contentAsString(result) must include("Invalid email or password")
    }
  }
}
