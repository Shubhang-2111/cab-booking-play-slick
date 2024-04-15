package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Future

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

    "UserController" should {

      "render create user form" in {
        val controller = inject[UserController]
        val request = FakeRequest(GET, "/create-user")
        val result: Future[Result] = controller.createUserForm.apply(request)

        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
        contentAsString(result) must include("")
      }

      "create user successfully" in {
        val controller = inject[UserController]
        val request = FakeRequest(POST, "/create-user").withFormUrlEncodedBody(
          "user_id" -> "1",
          "name" -> "John",
          "last_name" -> "Doe",
          "email" -> "john@example.com",
          "password" -> "password"
        )
        val result: Future[Result] = route(app, request).get

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some("/booking-page/john@example.com")
      }

      "return BadRequest if form data is invalid" in {
        val request = FakeRequest(POST, "/create-user").withFormUrlEncodedBody(
          "user_id" -> "1",
          "name" -> "",
          "last_name" -> "",
          "email" -> "john@example.com",
          "password" -> ""
        )
        val result: Future[Result] = route(app, request).get

        status(result) mustBe BAD_REQUEST
      }
  }
}
