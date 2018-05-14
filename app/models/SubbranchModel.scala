package models

import dao._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubbranchModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends Tables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  def search(name:String,city:String,min:Double,max:Double) : Future[Seq[SubbranchRow]] = db.run {
    Subbranch.filter { item =>
      (item.bankName like ("%"++name++"%")) &&
      (item.city like ("%"++city++"%")) &&
      (item.money >= min && item.money <= (if(max==0) Double.MaxValue else max))
    }.result
  }

  def insert(row:SubbranchRow):Future[Int] = db.run {
    Subbranch += row
  }

  def update(old_row:SubbranchRow,new_row:SubbranchRow):Future[Int] = db.run {
    Subbranch.filter(_.bankName===old_row.bankName).update(new_row)
  }

  def delete(name:String):Future[Int] = db.run {
    Subbranch.filter{_.bankName===name}.delete
  }

}
