package controllers

import com.google.inject.Singleton
import models.{BookingDAO, Cabs, CabsDAO, City, DriverDAO}
import models.PrivateExecutionContext.ec

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future

@Singleton
class BookingController @Inject()(cabsDAO: CabsDAO,bookingDAO: BookingDAO,driverDAO: DriverDAO)(val controllerComponents: ControllerComponents) extends BaseController {

  def bookingPage(userEmail:String): Action[AnyContent] = Action.async(implicit request => {
    val allCabsFuture: Future[Seq[Cabs]] = cabsDAO.demoReadAllCabs()
    val allCitiesFuture: Future[Seq[City]] = cabsDAO.demoReadCities()
    for {
      allCabs <- allCabsFuture
      allCities <- allCitiesFuture
    } yield Ok(views.html.booking(allCabs, userEmail, allCities))
  })


  def calculateDistance(): Action[AnyContent] = Action.async { implicit request =>
    val formData = request.body.asFormUrlEncoded
    val sourceCityId = formData.flatMap(_.get("source").flatMap(_.headOption)).getOrElse("1")
    val destinationCityId = formData.flatMap(_.get("destination").flatMap(_.headOption)).getOrElse("1")
    val selectedCab = formData.flatMap(_.get("cab").flatMap(_.headOption)).getOrElse("1")
    val user = formData.flatMap(_.get("user").flatMap(_.headOption)).getOrElse("1")

    println("Selected Cab is "+selectedCab)

    val selectedCabName = bookingDAO.getCabName(selectedCab.toInt)

    val ratings = bookingDAO.findAverageRating(selectedCabName,sourceCityId.toInt,destinationCityId.toInt)

    val distanceFuture = cabsDAO.findDistance(sourceCityId.toInt,destinationCityId.toInt)
    val (sourceCityName,destinationCityName) = (cabsDAO.findCityName(sourceCityId.toInt),
      cabsDAO.findCityName(destinationCityId.toInt))

    distanceFuture.map{
      case Some(distance) =>
        val fare = cabsDAO.calculateFare(distance.toInt,selectedCabName)
        Ok(views.html.bookingConfirmation(sourceCityId.toInt,destinationCityId.toInt,sourceCityName,destinationCityName, distance.toInt, selectedCabName, user, fare,ratings,selectedCab.toInt))
      case None =>
        NotFound("Destination and Source cannot be Same")
    }
  }

  def confirmBooking: Action[AnyContent] = Action { implicit request =>
    val sourceCityId = request.body.asFormUrlEncoded.get("sourceCityId").head
    val destinationCityId = request.body.asFormUrlEncoded.get("destinationCityId").head
    val distance = request.body.asFormUrlEncoded.get("distance").head.toInt
    val selectedCab = request.body.asFormUrlEncoded.get("selectedCab").head
    val user = request.body.asFormUrlEncoded.get("user").head
    val fare = request.body.asFormUrlEncoded.get("fare").head.toDouble
    val selectedCabId = request.body.asFormUrlEncoded.get("cabId").head.toInt

    bookingDAO.insertBooking(user,selectedCab,sourceCityId.toInt,destinationCityId.toInt,selectedCabId)
    bookingDAO.demoUpdate(selectedCab)

    val publicKey = driverDAO.getPublicKey(selectedCabId)

//    Ok(views.html.ratings(sourceCityId.toInt, destinationCityId.toInt, selectedCab))
    Ok(views.html.payment(publicKey,fare*0.0000000001,sourceCityId.toInt,destinationCityId.toInt,selectedCab))
  }

  def previousBookings(user:String): Action[AnyContent] = Action.async { implicit request=>
    val allBookings = bookingDAO.readAllPreviousBooking(user)
    for {
      booking <- allBookings
    } yield Ok(views.html.previousBooking(booking))
  }

  def ratings(sourceCityId: Int, destinationCityId: Int, selectedCabId: String): Action[AnyContent] = Action{ implicit request =>
    Ok(views.html.ratings(sourceCityId, destinationCityId, selectedCabId))
  }
  def submitRating: Action[AnyContent] = Action { implicit request =>

    val sourceCityId = request.body.asFormUrlEncoded.get("sourceCityId").head
    val destinationCityId = request.body.asFormUrlEncoded.get("destinationCityId").head
    val rating = request.body.asFormUrlEncoded.get("rating").head
    val cab = request.body.asFormUrlEncoded.get("selectedCab").head

    bookingDAO.insertRating(cab,sourceCityId.toInt,destinationCityId.toInt,rating.toInt)
    Ok("Thank For Journey!!!")
  }

}
