package models

import java.sql.Date

import dao._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/* Entity class storing rows of table Staff
*  @param staffIdCard Database column staff_id_card SqlType(CHAR), PrimaryKey, Length(255,false)
*  @param bankName Database column bank_name SqlType(CHAR), Length(64,false)
*  @param name Database column name SqlType(CHAR), Length(64,false)
*  @param phone Database column phone SqlType(CHAR), Length(16,false)
*  @param address Database column address SqlType(TEXT)
*  @param startDate Database column start_date SqlType(DATE) */

@Singleton
class StaffModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  def search(staffIdCard:String,name:String,bankName:String,minDate:Date,maxDate:Date) : Future[Seq[StaffRow]] = db.run {
    val query = if(staffIdCard.isEmpty){
      Staff.filter { item =>
        (item.name like ("%"++name++"%")) &&
        (item.bankName like ("%"++bankName++"%")) &&
        (item.startDate>=minDate && item.startDate<=maxDate)
      }.result
    }else{
      Staff.filter { item =>
        (item.staffIdCard===staffIdCard) &&
        (item.name like ("%"++name++"%")) &&
        (item.bankName like ("%"++bankName++"%")) &&
        (item.startDate>=minDate && item.startDate<=maxDate)
      }.result
    }
    query
  }

  def insert(row:StaffRow):Future[Int] = db.run {
    Staff += row
  }

  def update(old_row:StaffRow,new_row:StaffRow):Future[Int] = db.run {
    Staff.filter(_.staffIdCard===old_row.staffIdCard).update(new_row)
  }

  def delete(staffIdCard:String):Future[Int] = db.run {
    println(staffIdCard)
    Staff.filter{_.staffIdCard===staffIdCard}.delete
  }

}
