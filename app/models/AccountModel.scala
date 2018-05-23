package models

import java.sql.Date

import dao.{DBTables, HasAccountRow}
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


  def search(accountId: String, account_type: Int, customerId: String, bankName: String, minMoney: Double, maxMoney: Double, minDate: Date, maxDate: Date): Future[Seq[BaseAccountRow]] = {
    lazy val saving_result = db.run {
      SavingAccount.filter { item =>
        if (accountId.isEmpty) {
          (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        } else {
          (item.accountId === accountId) &&
            (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }
      }.flatMap { account =>
        HasAccount.filter { has =>
          if (customerId.isEmpty) {
            has.accountId === account.accountId
          } else {
            (has.accountId === account.accountId) &&
              (has.idCard === customerId)
          }
        }.map(has => (account, has))
      }.result
    }
    lazy val checking_result = db.run {
      CheckingAccount.filter { item =>
        if (accountId.isEmpty) {
          (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        } else {
          (item.accountId === accountId) &&
            (item.bankName like ("%" ++ bankName ++ "%")) &&
            (item.money >= minMoney && item.money <= maxMoney) &&
            (item.date >= minDate && item.date <= maxDate)
        }
      }.flatMap { account =>
        HasAccount.filter { has =>
          if (customerId.isEmpty) {
            has.accountId === account.accountId
          } else {
            (has.accountId === account.accountId) &&
              (has.idCard === customerId)
          }
        }.map(has => (account, has))
      }.result
    }

    if (account_type == 0) {
      for (x <- saving_result) yield x.map { item => BaseAccountRow.make(item._1, item._2.idCard) }
    } else if (account_type == 1) {
      for (y <- checking_result) yield y.map { item => BaseAccountRow.make(item._1, item._2.idCard) }
    } else {
      for (x <- checking_result;
           y <- saving_result)
        yield x.map { item => BaseAccountRow.make(item._1, item._2.idCard) } ++ y.map { item => BaseAccountRow.make(item._1, item._2.idCard) }
    }
  }

  def insert(row0: BaseAccountRow): Future[Int] = {
    val row = row0.copy(accountId = new java.util.Date().getTime.toString)
    db.run {
      Account.flatMap { account =>
        HasAccount
          .filter(_.accountId === account.accountId)
          .map(x => (x.isSaving, account.bankName))
      }.result
    }.flatMap { accounts =>
      db.run {
        val stmt = {
          if (accounts.exists { account =>
            val is_saving = account._1.getOrElse(false)
            is_saving == row.isSaving && account._2 == row.bankName.getOrElse("")
          }) {
            throw new RuntimeException()
          } else {
            (Account += row.toAccount).flatMap { _ =>
              (if (row.isSaving) {
                SavingAccount += row.toSaving
              } else {
                CheckingAccount += row.toChecking
              }).flatMap { _ =>
                HasAccount += HasAccountRow(row.accountId, row.customerId, Some(new Date(new java.util.Date().getTime)), Some(row.isSaving))
              }
            }
          }
        }
        stmt
      }
    }
  }

  def update(old_row: BaseAccountRow, new_row: BaseAccountRow): Future[Int] = db.run {
    if (old_row.isSaving != new_row.isSaving || old_row.accountId != new_row.accountId) {
      throw new MatchError("")
    } else {
      Account.filter(_.accountId===old_row.accountId).update(new_row.toAccount).flatMap{_=>
        if (old_row.isSaving) {
          SavingAccount.filter(_.accountId === old_row.accountId).update(new_row.toSaving)
        } else {
          CheckingAccount.filter(_.accountId === old_row.accountId).update(new_row.toChecking)
        }
      }
    }
  }

  def delete(account_id: String): Future[Int] = db.run {
    CheckingAccount.filter(_.accountId === account_id).delete.flatMap { _ =>
      SavingAccount.filter(_.accountId === account_id).delete.flatMap { _ =>
        HasAccount.filter(_.accountId === account_id).delete.flatMap { _ =>
          Account.filter(_.accountId === account_id).delete
        }
      }
    }
  }

}
