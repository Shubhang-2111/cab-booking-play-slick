package models

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class
CabsDAOSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  // Assuming you have mock implementations for Tables and db

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
      val cityId = 1 // Assuming cityId exists in your test data
      val cityName = cabsDAO.findCityName(cityId)
        cityName mustBe  "New York" // Assert the expected city name
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
      val distance = 50 // Assuming distance in kilometers
      val selectedCabName = "Toyota Innova" // Assuming selectedCabName exists in your test data
      val fare = cabsDAO.calculateFare(distance, selectedCabName)
        fare mustBe 7500
    }
  }
}
