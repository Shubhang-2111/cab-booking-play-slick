package models

import java.sql.Timestamp

class Tables {

  import slick.jdbc.PostgresProfile.api._

  class UsersTable(tag: Tag) extends Table[Users](tag,Some("cabs"),"Users") {
    def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def lastName = column[String]("last_name")

    def email = column[String]("email")

    def password = column[String]("password")

    override def * = (id, name, lastName, email, password) <> (Users.tupled, Users.unapply)

  }

  class CabsTable(tag: Tag) extends Table[Cabs](tag, Some("cabs"), "Cab_details") {
    def cabId = column[Int]("cab_id",O.PrimaryKey ,O.AutoInc)
    def cabName = column[String]("cab_name")

    def fare = column[Float]("fare")

    def seater = column[Int]("seater")

    def driverId = column[Int]("driver_id",O.AutoInc)

    def driverName = column[String]("driver_name")

    def status = column[Boolean]("status")

    override def * = (cabId,cabName,fare,seater,driverId,driverName,status) <> (Cabs.tupled, Cabs.unapply)

  }

  class DistancesTable(tag: Tag) extends Table[Distance](tag, Some("cabs"), "Distances") {
    def distance_id = column[Int]("distance_id", O.PrimaryKey)

    def from_city_id = column[Int]("from_city_id")

    def to_city_id = column[Int]("to_city_id")

    def distance_km = column[Float]("distance_km")

    def * = (distance_id, from_city_id, to_city_id, distance_km) <> (Distance.tupled, Distance.unapply)
  }

  // Define the Cities table
  class CitiesTable(tag: Tag) extends Table[City](tag, Some("cabs"), "Cities") {
    def city_id = column[Int]("city_id", O.PrimaryKey)

    def city_name = column[String]("city_name")

    def * = (city_id, city_name) <> (City.tupled, City.unapply)
  }

  class BookingTable(tag: Tag) extends Table[Bookings](tag, Some("cabs"),"Bookings") {
    def booking_id = column[Int]("booking_id", O.PrimaryKey, O.AutoInc)

    def user_id = column[String]("user_id")

    def cab = column[String]("cab")

    def booking_date = column[Timestamp]("booking_date", O.Default(new Timestamp(System.currentTimeMillis())))

    def from_city_id = column[Int]("from_city_id")

    def to_city_id = column[Int]("to_city_id")

    def cab_id = column[Int]("cab_id")

    def * = (booking_id, user_id, cab, booking_date, from_city_id, to_city_id,cab_id) <> (Bookings.tupled, Bookings.unapply)
  }


  class RatingsTable(tag: Tag) extends Table[Ratings](tag, Some("cabs"),"Ratings") {
    def ratingId = column[Long]("rating_id", O.PrimaryKey,O.AutoInc)

    def cabId = column[Int]("cab_id")

    def fromCityId = column[Int]("from_city_id")

    def toCityId = column[Int]("to_city_id")

    def rating = column[Int]("rating")

    override def * = (ratingId,cabId, fromCityId, toCityId, rating) <> (Ratings.tupled, Ratings.unapply)
  }

  class DriverTable(tag: Tag) extends Table[Driver](tag, Some("cabs"), "Driver") {
    def driverId = column[Int]("driver_id", O.PrimaryKey, O.AutoInc)

    def driverEmail = column[String]("driver_email")

    def driverPassword = column[String]("driver_password")

    def driverName = column[String]("driver_name")

    def cabName = column[String]("cab_name")

    def publicKey = column[String]("public_key")

    override def * = (driverId,driverEmail,driverPassword,driverName,cabName,publicKey) <> (Driver.tupled, Driver.unapply)
  }

  lazy val userTable = TableQuery[UsersTable]
  lazy val cabsTable = TableQuery[CabsTable]
  lazy val cityTable = TableQuery[CitiesTable]
  lazy val distanceTable = TableQuery[DistancesTable]
  lazy val bookingTable = TableQuery[BookingTable]
  lazy val ratingsTable = TableQuery[RatingsTable]
  lazy val driverTable = TableQuery[DriverTable]

}
