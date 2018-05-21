package models

import dao.DBTables
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import myutils.MyUtils._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class StatModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  def stat(isSaving:Boolean,minDate:java.sql.Date,maxDate:java.sql.Date): Future[Seq[StatResult]] = db.run {
    println(minDate,maxDate)
    if(isSaving) {
      SavingAccount
        .filter(account => account.date >= minDate && account.date < maxDate)
        .groupBy(_.bankName)
        .map(res => (res._1, res._2.map(_.money).sum, res._2.map(_.accountId).countDistinct))
        .result
    }else{
      CheckingAccount
        .filter(account => account.date >= minDate && account.date < maxDate)
        .groupBy(_.bankName)
        .map(res => (res._1, res._2.map(_.money).sum, res._2.map(_.accountId).countDistinct))
        .result
    }
  }.map(_.map(item=>StatResult(item._1.getOrElse(""),item._2.getOrElse(0),item._3)))
}