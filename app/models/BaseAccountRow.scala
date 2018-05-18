package models

import java.sql.Date

import dao.{AccountRow, CheckingAccountRow, SavingAccountRow}

object BaseAccountRow{
  def make(x:CheckingAccountRow,customer:String): BaseAccountRow = new BaseAccountRow(false,x.accountId, x.bankName,customer, x.money, x.date, x.creditLine, "")
  def make(y:SavingAccountRow,customer:String): BaseAccountRow = new BaseAccountRow(true,y.accountId, y.bankName, customer,y.money, y.date, y.rate,y.currency)
}
// customerId:String,
case class BaseAccountRow(isSaving:Boolean,accountId: String,bankName: Option[String] = None,customerId:String,money: Double, date: java.sql.Date, rate_creditLine: Double, currency: String){
  implicit def toSaving: SavingAccountRow = SavingAccountRow(accountId,bankName,money,date,rate_creditLine,currency)
  implicit def toChecking: CheckingAccountRow = CheckingAccountRow(accountId,bankName,money,date,rate_creditLine)
  implicit def toAccount:AccountRow = AccountRow(accountId,bankName.getOrElse(""),money,date)
}
