package controllers

import com.google.inject.Singleton
import models.{Cabs, Driver, Login, PrivateExecutionContext,CombinedData}
import javax.inject.Inject
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats.floatFormat

import scala.concurrent.Future

@Singleton
class DriverController @Inject()(driverDAO: models.DriverDAO)(val controllerComponents: ControllerComponents) extends BaseController {

  val combinedForm: Form[CombinedData] = Form(
    mapping(
      "driver_id" -> number,
      "driver_name" -> text,
      "driver_email" -> text,
      "driver_password" -> text,
      "public_key" -> text,
      "cab_name" -> text,
      "fare" -> of[Float],
      "seater" -> number,
    )(CombinedData.apply)(CombinedData.unapply)
  )


  val loginForm: Form[Login] = Form(
    mapping(
      "Email" -> text,
      "Password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  def showLoginForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.driverLogin(loginForm)(messagesApi.preferred(request)))
  }

  def login: Action[AnyContent] = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.driverLogin(formWithErrors)(messagesApi.preferred(request))))
      },
      loginData => {
        driverDAO.authenticate(loginData.email, loginData.password).map {
          case Some(driver) => Redirect(controllers.routes.DriverController.driverPage(driver.driverId))
          case None => BadRequest(views.html.invalidLogin("Driver"))
        }(PrivateExecutionContext.ec)
      }
    )
  }

  def driverPage (driverId:Int): Action[AnyContent] = Action{ implicit request =>

    val driver = driverDAO.getDriverDetails(driverId)
    val driverRating = driverDAO.getDriverRating(driverId)
    val bookings = driverDAO.getAllBookingsByDriver(driverId)

    val totalIncome = driverDAO.totalIncome(bookings)

    Ok(views.html.driverProfile(driver,driverRating,bookings,totalIncome))
  }

  def createDriverForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createDriver(combinedForm)(messagesApi.preferred(request)))
  }

  def createDriverAndCab: Action[AnyContent] = Action.async { implicit request =>
    combinedForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.createDriver(formWithErrors)(messagesApi.preferred(request))))
      },
      combinedData => {
        val driver = Driver(combinedData.driverId, combinedData.driverEmail, combinedData.driverPassword, combinedData.driverName, combinedData.cabName,combinedData.publicKey)
        val cab = Cabs(100, combinedData.cabName, combinedData.fare, combinedData.seater, driver.driverId, driver.driverName,false)
        driverDAO.insertNewDriver(driver)
        driverDAO.insertNewCab(cab)
        Future.successful(Ok(views.html.driverProfile(driver,0,Seq(),0)))
      }
    )
  }
}