package models

import java.sql.Date

import dao.{CustomerRow, DBTables}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/* Entity class storing rows of table Customer
*  @param idCard Database column id_card SqlType(CHAR), PrimaryKey, Length(64,false)
*  @param staffIdCard Database column staff_id_card SqlType(CHAR), Length(255,false), Default(None)
*  @param name Database column name SqlType(CHAR), Length(64,false)
*  @param phone Database column phone SqlType(CHAR), Length(16,false)
*  @param address Database column address SqlType(TEXT)
*  @param contactName Database column contact_name SqlType(CHAR), Length(64,false)
*  @param contactPhone Database column contact_phone SqlType(CHAR), Length(16,false)
*  @param contactEmail Database column contact_email SqlType(CHAR), Length(255,false)
*  @param contactRole Database column contact_role SqlType(CHAR), Length(64,false)
*  @param role Database column role SqlType(CHAR), Length(16,false), Default(None) */

@Singleton
class CustomerModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  def search(idCard:String,staffIdCard:String,name:String,contactName:String) : Future[Seq[CustomerRow]] = db.run {
    if(idCard.isEmpty){
      Customer.filter { item =>
        (item.name like ("%"++name++"%")) &&
        (item.staffIdCard.ifNull("") like ("%"++staffIdCard++"%")) &&
        (item.contactName like ("%"++contactName++"%"))
      }.result
    }else{
      Customer.filter { item =>
        (item.idCard === idCard) &&
        (item.name like ("%"++name++"%")) &&
        (item.staffIdCard.ifNull("") like ("%"++staffIdCard++"%")) &&
        (item.contactName like ("%"++contactName++"%"))
      }.result
    }
  }


  def insert(row:CustomerRow):Future[Int] = db.run {
    Customer += row
  }

  def update(old_row:CustomerRow,new_row:CustomerRow):Future[Int] = db.run {
    Customer.filter(_.idCard===old_row.idCard).update(new_row)
  }

  def delete(id_card:String):Future[Int] = db.run {
    Customer.filter{_.idCard === id_card}.delete
  }
}
