package models

import dao._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import myutils.MyUtils._

import scala.concurrent.{ExecutionContext, Future}

/* Entity class storing rows of table Loan
*  @param loanNumber Database column loan_number SqlType(CHAR), PrimaryKey, Length(255,false)
*  @param bankName Database column bank_name SqlType(CHAR), Length(64,false)
*  @param amount Database column amount SqlType(DOUBLE) */

/* Entity class storing rows of table CustomerLoan
*  @param idCard Database column id_card SqlType(CHAR), Length(64,false)
*  @param loanNumber Database column loan_number SqlType(CHAR), Length(255,false) */

/* Entity class storing rows of table LoanPay
*  @param payId Database column pay_id SqlType(CHAR), PrimaryKey, Length(255,false)
*  @param loanNumber Database column loan_number SqlType(CHAR), Length(255,false)
*  @param date Database column date SqlType(DATE)
*  @param money Database column money SqlType(DOUBLE) */

@Singleton
class LoanModel @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTables {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import profile.api._
  import dbConfig._

  def search(loanNumber: String, bankName: String, customerID: Seq[String], minMoney: Double, maxMoney: Double): Future[Seq[LoanRow]] = {
    val loanNumbers = customerID.foldLeft[Future[Seq[String]]](Future(Seq(loanNumber))) { (acc: Future[Seq[String]], customer) =>
      for (x <- acc;
           y <- db.run(CustomerLoan.filter(_.idCard === customer).result))
        yield x intersect y.map(_.loanNumber)
    }
    val result = db.run {
      Loan.filter { item =>
        (item.bankName like ("%" ++ bankName ++ "%")) &&
          (item.amount >= minMoney && item.amount <= (if (maxMoney == 0) Double.MaxValue else maxMoney))
      }.result
    }
    for (x <- loanNumbers; y <- result)
      yield y.filter(item =>
        x.contains(item.loanNumber) ||
          (if (loanNumber.nonEmpty) item.loanNumber == loanNumber else false)
      )
  }

  def getCustomers(loanNumber: String): Future[Seq[CustomerRow]] = db.run {
    (for (x <- CustomerLoan;
          y <- Customer if x.loanNumber === loanNumber && x.idCard === y.idCard)
      yield y).result
  }

  // state:0 -> 1 -> 2
  def getPay(loanNumber: String): Future[(Int, Seq[LoanPayRow])] = {
    db.run {
      LoanPay.filter(_.loanNumber === loanNumber).result
    } flatMap { pay =>
      db.run(Loan.filter(_.loanNumber === loanNumber).result.head) map { loan =>
        val amount = loan.amount
        val state =
          if (pay.isEmpty) 0
          else if (pay.foldLeft(0.0) { (acc, x) => acc + x.money } == amount) 2
          else 1
        (state, pay)
      }
    }
  }

  def insertLoan(loan: LoanRow, customers: Seq[String]): Future[Option[Int]] = db.run {
    Loan += loan.copy(loanNumber = new java.util.Date().getTime.toString)
    CustomerLoan ++= customers map (x => CustomerLoanRow(loan.loanNumber, x))
  }

  def insertPay(pay: LoanPayRow): Future[Int] =
    for ((state, _) <- getPay(pay.loanNumber);
         result <- db.run {
           if (state == 2) throw new RuntimeException()
           else LoanPay += pay.copy(payId = new java.util.Date().getTime.toString)
         }
    ) yield result

  def delete(loanNumber:String): Future[Int] = {
    for((state,_)<-getPay(loanNumber);
        result <- db.run {
          if(state==1) {
            throw new RuntimeException()
          } else {
            LoanPay.filter(_.loanNumber===loanNumber).delete
            CustomerLoan.filter(_.loanNumber===loanNumber).delete
            Loan.filter(_.loanNumber===loanNumber).delete
          }
        }
    ) yield result
  }
}
