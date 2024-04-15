package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest

class BookingDAOSpec extends PlaySpec with GuiceOneAppPerTest with ScalaFutures {

  import models.PrivateExecutionContext._

  val tablesMock = new models.Tables
  val bookingDAO = new BookingDAO(tablesMock)

  "BookingDAO" must {


    "read all previous booking" in {

      val required = bookingDAO.readAllPreviousBooking("TestUser")
      required must not be 0
    }

    " get cab name" in {
      val required = bookingDAO.getCabName(1)

      required mustBe "Honda City"

    }


  }
}
