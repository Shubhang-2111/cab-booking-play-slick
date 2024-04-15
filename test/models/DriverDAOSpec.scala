package models

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import org.scalatest.concurrent.ScalaFutures
import models.Connection.db

import scala.concurrent.Await
import scala.concurrent.duration._

class DriverDAOSpec extends PlaySpec with GuiceOneAppPerTest with ScalaFutures {

  import slick.jdbc.PostgresProfile.api._
  import models.PrivateExecutionContext._

  val tablesMock = new models.Tables
  val driverDAO = new DriverDAO(tablesMock)

  "DriverDAO" must {

    "insert Driver" in {

      val required = Await.result(
        db.run(tablesMock.driverTable
          .filter(el => el.driverEmail === "testdriver@gmail.com")
          .result), Duration.Inf).size

      required mustBe 1
    }

    "authenticate Driver" in {
        val result = driverDAO.authenticate("testdriver@gmail.com", "123")

        result.map {
          case Some(driver) => driver.driverEmail mustBe "testdriver@gmail.com"
          case None => None
        }
      }

    "get driver rating " in {
      val result = driverDAO.totalIncome(Seq())

      result mustBe 0
    }

    "get driver details" in {
      val result = driverDAO.getDriverDetails(2)

      result.driverName mustBe "TestDriver"
    }

  }
}
