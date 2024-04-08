package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "LoginController" should {

    "render the home page" in {
      val controller = inject[HomeController]
      val request = FakeRequest(GET, "/")

      val result: Future[Result] = controller.index.apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Cab Book")
    }

  }
}
