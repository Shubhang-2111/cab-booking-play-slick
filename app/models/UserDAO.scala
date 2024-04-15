package models

import com.google.inject.Inject
import models.Connection.db

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


object PrivateExecutionContext{
  val executor = Executors.newFixedThreadPool(10)
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(executor)
}

class UserDAO @Inject()(tables: Tables){

  import slick.jdbc.PostgresProfile.api._
  import PrivateExecutionContext._

  def addUser(user: Users): Unit = {
    val futureId = db.run(tables.userTable += user)

        futureId.onComplete({
          case Success(value) => println(s"Processed ${value}")
          case Failure(exception) => println(s"Query Failed ${exception}")
  })(PrivateExecutionContext.ec)

   Thread.sleep(3000)
  }

  def authenticate(email: String, password: String): Future[Option[Users]] =
    db.run(tables.userTable.filter(user => user.email === email && user.password === password).result.headOption)


}







