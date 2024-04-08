package models

import com.google.inject.Inject
import models.Connection.db
import models.Tables

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class CabsDAO @Inject()(tables: Tables){

  import  slick.jdbc.PostgresProfile.api._
  import models.PrivateExecutionContext._


  def demoReadAllCabs(): Future[Seq[Cabs]] = {
    val resultFuture: Future[Seq[Cabs]] = db.run(tables.cabsTable.result)
    resultFuture
  }

  def demoReadCities():Future[Seq[City]] = {
    val resultFuture = db.run(tables.cityTable.result)
    resultFuture
  }

  def findCityName(cityId:Int):String={
    val result = Await.result(db.run(tables.cityTable
      .filter(_.city_id===cityId)
      .map(_.city_name)
      .result.headOption),Duration.Inf)
      .getOrElse("")
    result
  }

  def findDistance(sourceId: Int, destinationId: Int): Future[Option[Float]] = {
    val query = tables.distanceTable.filter(d => (d.from_city_id === sourceId && d.to_city_id === destinationId) || (d.from_city_id === destinationId && d.to_city_id === sourceId))
      .map(_.distance_km)
      .result
      .headOption
    db.run(query)
  }

  def calculateFare(distance: Int, selectedCabName: String): Int = {
    val query = tables.cabsTable.filter(c => c.cabName === selectedCabName).map(_.fare).result.headOption
    val fareFuture = db.run(query)

    val fareOption = Await.result(fareFuture, Duration.Inf)

    fareOption match {
      case Some(fare) => math.round(fare * distance)
      case None => throw new NoSuchElementException(s"Could not find fare for cab: $selectedCabName")
    }
  }

}