package models

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class
CabsDAOSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val tablesMock = new models.Tables
  val cabsDAO = new CabsDAO(tablesMock)

  "CabsDAO" must {
    "return all cities" in {
      val resultFuture: Future[Seq[City]] = cabsDAO.demoReadCities()
      resultFuture.map { result =>
        result must not be empty
      }
    }

    "find city name by id" in {
      val cityId = 1
      val cityName = cabsDAO.findCityName(cityId)
        cityName mustBe  "New York"
    }

    "find distance between cities" in {
      val sourceId = 1
      val destinationId = 2
      val distanceFuture = cabsDAO.findDistance(sourceId, destinationId)
      distanceFuture.map { distance =>
        distance mustBe  399
      }
    }

    "calculate fare correctly" in {
      val distance = 50
      val selectedCabName = "Honda City"
      val fare = cabsDAO.calculateFare(distance, selectedCabName)
        fare mustBe 7500
    }
  }
}
