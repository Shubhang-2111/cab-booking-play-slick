package controllers

import com.google.inject.Singleton
import models.{Login, PrivateExecutionContext, UserDAO}

import javax.inject.Inject
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import scala.concurrent.Future

@Singleton
class LoginController @Inject()(userDAO: UserDAO)(val controllerComponents: ControllerComponents) extends BaseController {

  val loginForm: Form[Login] = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply)
  )

  def showLoginForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(loginForm)(messagesApi.preferred(request)))
  }

  def login: Action[AnyContent] = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.login(formWithErrors)(messagesApi.preferred(request))))
      },
      loginData => {
        userDAO.authenticate(loginData.email, loginData.password).map {
          case Some(user) => Redirect(controllers.routes.BookingController.bookingPage(loginData.email))
          case None => Unauthorized("Invalid email or password")
        }(PrivateExecutionContext.ec)
      }
    )
  }
}
