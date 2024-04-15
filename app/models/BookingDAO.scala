package models


import com.google.inject.Inject
import models.Connection.db
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class BookingDAO @Inject()(tables: Tables){

  import slick.jdbc.PostgresProfile.api._
  import models.PrivateExecutionContext._

  def insertBooking(user: String, cab: String, from_city_id: Int, to_city_id: Int,cab_id:Int,fare:Int): Unit = {
    val time = java.sql.Timestamp.from(java.time.Instant.now())

    val newValue = Bookings(1,user,cab,time,from_city_id,to_city_id,cab_id,fare)

    val insertFuture = db.run(tables.bookingTable+=newValue)

    insertFuture.onComplete {
      case Success(_) => println("Insertion successful")
      case Failure(ex) => println(s"Error inserting data: ${ex.getMessage}")
    }
  }


  def readAllPreviousBooking(user:String): Future[Seq[Bookings]] = {

    val resultFuture: Future[Seq[Bookings]] = db.run(tables.bookingTable
      .filter(_.user_id === user)
      .result)

    resultFuture
  }

  def demoUpdate(selectedCabName: String): Unit = {
    val futureResult = db.run(tables.cabsTable
      .filter(_.cabName === selectedCabName)
      .map(_.status)
      .update(true))


    futureResult.onComplete {
      case Success(rowsAffected) =>
        if (rowsAffected > 0) {
          println(s"Status updated for cab $selectedCabName")
        } else {
          println(s"No cab found with name $selectedCabName")
        }
      case Failure(exception) =>
        println(s"Failed to update status: ${exception.getMessage}")
    }

    Thread.sleep(2000)

    }

  def insertRating(selectedCab:String,sourceId:Int,destinationId:Int,rating:Int):Unit={

    println("Cab for rating is "+selectedCab)

    val cabId = Await.result(db.run(tables.cabsTable
      .filter(_.cabName === selectedCab)
      .map(_.cabId)
      .result
      .headOption), Duration.Inf).getOrElse(0)


    val newValues = Ratings(1,cabId,sourceId,destinationId,rating)

    val result =  db.run(tables.ratingsTable+=newValues)
    result.onComplete{
      case Success(value) => println("Inserted in Ratings")
      case Failure(er) => println("Failed due to "+er.getMessage)
    }

  }

  def findAverageRating(cab:String,sourceId: Int, destinationId: Int): Int = {

    println("Source id " + sourceId)
    println("destination id" + destinationId)
    println("cab name"+cab)

    val cabId = Await.result(db.run(tables.cabsTable
      .filter(_.cabName===cab)
      .map(_.cabId)
      .result
      .headOption),Duration.Inf).getOrElse(0)


    val required = Await.result(
      db.run(tables.ratingsTable
        .filter(el => el.cabId === cabId)
        .map(_.rating)
        .avg
        .result),Duration.Inf).getOrElse(0)

    required
  }


  def getCabName(cabId:Int):String={ Await.result(
    db.run(tables.cabsTable
      .filter(_.cabId === cabId)
      .map(_.cabName).result.headOption),Duration.Inf).getOrElse("No Cab Exist")
  }
 }
