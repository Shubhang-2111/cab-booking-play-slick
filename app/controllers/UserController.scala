package controllers

import com.google.inject.Singleton
import models.{PrivateExecutionContext, UserDAO, Users}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.Future

@Singleton
class UserController @Inject()(userDAO: UserDAO)(val controllerComponents: ControllerComponents)
  extends BaseController {

  val userForm = Form(
    mapping(
      "user_id" -> longNumber,
      "name" -> text,
      "last_name" -> text,
      "email" -> text,
      "password" -> text
    )(Users.apply)(Users.unapply)
  )

  def createUserForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createUser(userForm)(messagesApi.preferred(request)))
  }

  def createUser: Action[AnyContent] = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.createUser(formWithErrors)(messagesApi.preferred(request))))
      },
      user => {
        userDAO.addUser(user)
        Future.successful(Redirect(controllers.routes.BookingController.bookingPage(user.email)))
      }
    )
  }

}
