package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Future

class BookingControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "BookingController" should {

    "show login form" in {
      val controller = inject[BookingController]
      val request = FakeRequest(GET, "/booking")
      val result: Future[Result] = controller.bookingPage("testuser@gmail.com").apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Booking")
    }

    "show previous booking" in {
      val controller = inject[BookingController]
      val request = FakeRequest(GET,"/previousBookings")

      val result = controller.previousBookings("testuser@gmail.com").apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Previous Bookings")

    }

  }
}
