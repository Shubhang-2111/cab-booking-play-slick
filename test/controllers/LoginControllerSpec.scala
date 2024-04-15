package controllers

import models.PrivateExecutionContext.ec
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Future
import scala.util.{Failure, Success}

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

    "redirect to booking page upon successful authentication" in {

      val controller = inject[LoginController]

      val request = FakeRequest(GET, "/login")
        .withFormUrlEncodedBody("Email" -> "testuser@gmail.com", "Password" -> "123")
        .withSession("email" -> "testuser@gmail.com")

      val result: Future[Result] = controller.login().apply(request)


      val redirect = redirectLocation(result)
      redirect.get mustEqual routes.BookingController.bookingPage("testuser@gmail.com").url
    }

    "reject authentication with invalid credentials" in {
      val controller = inject[LoginController]
      val request = FakeRequest(POST, routes.LoginController.login.url)
        .withFormUrlEncodedBody("email" -> "invalid@example.com", "password" -> "invalidPassword")
      val result = controller.login().apply(request)

      status(result) mustBe BAD_REQUEST
    }

  }
}
