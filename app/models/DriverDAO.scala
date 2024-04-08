package models

import com.google.inject.Inject
import models.Connection.db

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class DriverDAO @Inject()(tables: Tables){

  import slick.jdbc.PostgresProfile.api._
  import models.PrivateExecutionContext._

  def insertNewDriver(driver: Driver):Unit={
    val result = db.run(tables.driverTable += driver)
    result.onComplete{
      case Success(value)=> println("Driver Inserted")
      case Failure(exception) => println("Driver Insertion Failed " + exception.getMessage)
    }
  }

  def insertNewCab(cab: Cabs): Unit = {
    val result = db.run(tables.cabsTable += cab)
    result.onComplete {
      case Success(value) => println("Cab Inserted")
      case Failure(exception) => println("Cab Insertion Failed " + exception.getMessage)
    }
  }

  def authenticate(email: String, password: String): Future[Option[Driver]] = {
    db.run(tables.driverTable.filter(driver => driver.driverEmail === email && driver.driverPassword === password).result.headOption)
  }

  def getDriverDetails(driverId:Int):Driver={
    Await.result(
      db.run(
        tables.driverTable
          .filter(_.driverId === driverId)
          .result
          .headOption
      ),Duration.Inf
    ).getOrElse(Driver(1,"","","","",""))
  }

  def getCabId(driverId:Int):Int={
    val cabId = Await.result(
      db.run(tables.cabsTable
        .filter(_.driverId === driverId)
        .map(_.cabId)
        .result
        .headOption), Duration.Inf
    ).getOrElse(1)

    cabId
  }

  def getAllBookingsByDriver(driverId:Int):Seq[Bookings]={

    val cabId = getCabId(driverId)

    val result = Await.result(
      db.run(
        tables.bookingTable
          .filter(_.cab_id === cabId)
          .result
      ),Duration.Inf
    )
    result
  }

  def getDriverRating(driverId:Int):Int={

    val cabId = getCabId(driverId)

    val ratings= Await.result(
      db.run(tables.ratingsTable
      .filter(_.cabId === cabId)
      .map((_.rating))
      .avg
      .result),Duration.Inf
    ).getOrElse(0)

    ratings
  }

  def getPublicKey(cabId:Int):String={
    val driverId = Await.result(db.run(
      tables.cabsTable
        .filter(_.cabId === cabId)
        .map(_.driverId)
        .result.headOption),Duration.Inf
    ).getOrElse(1)

    val publicKey = Await.result(db.run(
      tables.driverTable
        .filter(_.driverId === driverId)
        .map(_.publicKey)
        .result
        .headOption),Duration.Inf
    ).getOrElse("Public Key Do Not exist for this driver")

    publicKey
  }


}
