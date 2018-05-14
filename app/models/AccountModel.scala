package models

import java.sql.Date

import dao.DBTables
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Reads, Writes}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/* Entity class storing rows of table Account
*  @param accountId Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false)
*  @param bankName Database column bank_name SqlType(CHAR), Length(64,false)
*  @param money Database column money SqlType(DOUBLE)
*  @param date Database column date SqlType(DATE) */

@Singleton
class AccountModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  implicit val writes1: Writes[BaseAccountRow] = Json.writes[BaseAccountRow]
  implicit val reads1: Reads[BaseAccountRow] = Json.reads[BaseAccountRow]

  def search(accountId: String, account_type: Int, bankName: String, minMoney: Double, maxMoney: Double, minDate: Date, maxDate: Date): Future[Seq[BaseAccountRow]] = {
    lazy val saving_result = db.run {
      if (accountId.isEmpty) {
        SavingAccount.filter { item =>
          (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }.result
      } else {
        SavingAccount.filter { item =>
          (item.accountId === accountId) &&
            (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }.result
      }
    }
    lazy val checking_result = db.run {
      if (accountId.isEmpty) {
        CheckingAccount.filter { item =>
          (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }.result
      } else {
        CheckingAccount.filter { item =>
          (item.accountId === accountId) &&
            (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }.result
      }
    }
    if (account_type == 0) {
      for (x <- saving_result) yield x.map { item => BaseAccountRow.make(item) }
    } else if (account_type == 1) {
      for (y <- checking_result) yield y.map { item => BaseAccountRow.make(item) }
    } else {
      for (x <- checking_result;
           y <- saving_result)
        yield x.map { item => BaseAccountRow.make(item) } ++ y.map { item => BaseAccountRow.make(item) }
    }
  }

  def insert(row: BaseAccountRow): Future[Int] = db.run {
    if (row.is_saving) {
      SavingAccount += row.toSaving
    } else {
      CheckingAccount += row.toChecking
    }
  }

  def update(old_row: BaseAccountRow, new_row: BaseAccountRow): Future[Int] = db.run {
    if (old_row.is_saving != new_row.is_saving || old_row.accountId != new_row.accountId) {
      throw new MatchError("")
    } else {
      if (old_row.is_saving) {
        SavingAccount.filter(_.accountId === old_row.accountId).update(new_row.toSaving)
      } else {
        CheckingAccount.filter(_.accountId === old_row.accountId).update(new_row.toChecking)
      }
    }
  }

  def delete(account_id: String): Future[Int] = db.run {
    CheckingAccount.filter {
      _.accountId === account_id
    }.delete
    SavingAccount.filter {
      _.accountId === account_id
    }.delete
  }

}
