package controllers

import java.sql.Date

import dao.{LoanPayRow, LoanRow}
import javax.inject.{Inject, Singleton}
import models.LoanModel
import play.api.mvc._
import myutils.MyUtils._
import play.api.libs.json.{Json, Reads, Writes}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class LoanController @Inject()(loan: LoanModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  case class ThisResult(count:Int,rows:Seq[LoanRow])
  case class InsertLoan(customers:Seq[String],loan:LoanRow)
  implicit val writes1: Writes[LoanRow] = Json.writes[LoanRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]
  implicit val reads1: Reads[LoanRow] = Json.reads[LoanRow]
  implicit val reads2: Reads[InsertLoan] = Json.reads[InsertLoan]
  implicit val reads3: Reads[LoanPayRow] = Json.reads[LoanPayRow]
  
  // loan_type: 0 for saving,1 for checking,2 for both
  def get(pg:Int,loanNumber: String, bankName: String, customerID: Seq[String], minMoney: Double, maxMoney: Double): Action[AnyContent] = Action.async {
    val res = loan.search(loanNumber,bankName,customerID,minMoney,if(maxMoney==0) Double.MaxValue else maxMoney)
    res.map {col=>
      Ok(ThisResult(col.size,col.slice(page_items*pg,page_items*(pg+1))).toJson)
    }
  }

  case class UpdateReq(old_row: LoanRow, new_row: LoanRow)

  implicit val reads: Reads[UpdateReq] = Json.reads[UpdateReq]

  def delete(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[LoanRow]
      loan.delete(req.loanNumber) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse {
      Future(BadRequest("request error"))
    }
  }


  def insert(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[InsertLoan]
      loan.insertLoan(req.loan,req.customers) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }

  def insertPay(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[LoanPayRow]
      loan.insertPay(req) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }

}
