package models


object Connection {
  import slick.jdbc.PostgresProfile.api._

  val db = Database.forConfig("postgres")
}
