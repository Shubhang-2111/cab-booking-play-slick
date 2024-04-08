package models

import java.sql.Timestamp

case class Bookings(
                     booking_id:Int,
                     user_id: String,
                     cab: String,
                     booking_date: Timestamp,
                     from_city_id: Int,
                     to_city_id: Int,
                     cab_id:Int
                   )
case class Users(id: Long, name: String, lstName: String,email :String, password: String)

case class Login(email: String, password: String)

case class Distance(distance_id: Int, from_city_id: Int, to_city_id: Int, distance_km: Float)

case class Cabs(cabId:Int,cabName:String,fare:Float,seater:Int,driverId:Int,driverName:String,status:Boolean)

case class City(city_id: Int, city_name: String)

case class Driver(driverId:Int,driverEmail:String,driverPassword:String,driverName:String,cabName:String,publicKey:String)

case class Ratings(ratingId:Long,cabId: Int, fromCityId: Int, toCityId: Int, rating: Int)

case class CombinedData(driverId: Int, driverName: String, driverEmail: String, driverPassword: String,publicKey:String, cabName: String, fare: Float, seater: Int)