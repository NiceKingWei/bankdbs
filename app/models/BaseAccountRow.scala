package models

import java.sql.Date

import dao.{CheckingAccountRow, SavingAccountRow}

object BaseAccountRow{

  def make(x:CheckingAccountRow): BaseAccountRow = new BaseAccountRow(false,x.accountId, x.bankName, x.money, x.date, x.creditLine, "")

  def make(y:SavingAccountRow): BaseAccountRow = new BaseAccountRow(true,y.accountId, y.bankName, y.money, y.date, y.rate,y.currency)
}

case class BaseAccountRow(is_saving:Boolean,accountId: String, bankName: Option[String] = None, money: Double, date: java.sql.Date, rate_creditLine: Double, currency: String){
  implicit def toSaving: SavingAccountRow = SavingAccountRow(accountId,bankName,money,date,rate_creditLine,currency)
  implicit def toChecking: CheckingAccountRow = CheckingAccountRow(accountId,bankName,money,date,rate_creditLine)
}
