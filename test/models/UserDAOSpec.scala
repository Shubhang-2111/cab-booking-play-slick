package models

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import org.scalatest.concurrent.ScalaFutures
import models.Connection.db
import scala.concurrent.Await
import scala.concurrent.duration._

class UserDAOSpec extends PlaySpec with GuiceOneAppPerTest with ScalaFutures {

  import slick.jdbc.PostgresProfile.api._
  import models.PrivateExecutionContext._

  val tablesMock = new models.Tables
  val userDAO = new UserDAO(tablesMock)

  "UserDAO" must {

    "insert User" in {

      val required = Await.result(
        db.run(tablesMock.userTable
          .filter(el => el.name === "Test")
          .result), Duration.Inf).size

      required mustBe 1
    }

    "authenticate user" in{
      val result = userDAO.authenticate("testuser@gmail.com","123")

      result.map{
        case Some(user) => user.name mustBe "Test"
        case None => None
      }
    }

  }
}
