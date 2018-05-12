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

  case class SubbranchSearch(name:String,city:String,min:Double,max:Double)
  implicit val SubbranchSearchReads: Reads[SubbranchSearch] = Json.reads[SubbranchSearch]

  import profile.api._
  import dbConfig._

  def search(search:SubbranchSearch) : Future[Seq[SubbranchRow]] = db.run {
    Subbranch.filter { item =>
      (item.bankName like (if(search.name.isEmpty) "%" else search.name)) &&
      (item.city like (if(search.city.isEmpty) "%" else search.city)) &&
      (item.money >= search.min && item.money <= (if(search.max==0) Double.MaxValue else search.max))
    }.result
  }

  def insert(name:String,city:String,money:Double):Future[Int] = db.run {
    Subbranch += SubbranchRow(name,city,money)
  }

  def update(name:String,city:String,money:Double):Future[Int] = db.run {
    Subbranch.filter(_.bankName===name).update(SubbranchRow(name,city,money))
  }

  def remove(name:String):Future[Int] = db.run {
    Subbranch.filter{_.bankName===name}.delete
  }

}
