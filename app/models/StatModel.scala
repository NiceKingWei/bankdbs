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

  def stat(isSaving: Boolean, minDate: java.sql.Date, maxDate: java.sql.Date): Future[Seq[StatResult]] =
    if (isSaving) {
      db.run {
        Account
          .filter(account => account.date >= minDate && account.date < maxDate)
          .groupBy(_.bankName)
          .map(res => (res._1, res._2.map(_.money).sum.getOrElse(0.0), res._2.map(_.accountId).countDistinct))
          .result
      }.map(f => f.map(item => StatResult(item._1, item._2, item._3)))
    } else {
      val res = db.run {
        (for (x <- Loan;
              y <- LoanPay if x.loanNumber === y.loanNumber) yield (x, y))
          .map(tp => (tp._1.bankName, tp._1.loanNumber, tp._2.money, tp._2.date))
          .filter(tp => tp._4 >= minDate && tp._4 < maxDate)
          .map(tp => (tp._1, tp._2, tp._3))
          .result
      }
      res.map { col =>
        col.groupBy(_._1).map { res =>
          val bankName = res._1
          val cols = res._2
          val money = cols.map(_._3).fold(0.0)(_ + _)
          val count = cols.map(_._2).toSet.size
          StatResult(bankName, money, count)
        }.toSeq
      }
    }
}