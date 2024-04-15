package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Future

class DriverControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "DriverController" should {

    "show login form" in {
      val controller = inject[DriverController]
      val request = FakeRequest(GET, "/driver-login")
      val result: Future[Result] = controller.showLoginForm.apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Driver Login")
    }


    "render Driver Profile " in {
      val controller= inject[DriverController]
      val request = FakeRequest(GET,"/driver-page")
      val result = controller.driverPage(1).apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Name")
    }



  }
}
