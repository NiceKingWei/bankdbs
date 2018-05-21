package controllers


import dao.{LoanPayRow, LoanRow}
import javax.inject.{Inject, Singleton}
import models.{ExLoanRow, LoanModel}
import play.api.mvc._
import myutils.MyUtils._
import play.api.libs.json.{Json, OWrites, Reads, Writes}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class LoanController @Inject()(loan: LoanModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  case class ThisResult(count:Int,rows:Seq[ExLoanRow])
  case class PayResult(rows:Seq[LoanPayRow])
  implicit val writes1: Writes[LoanRow] = Json.writes[LoanRow]
  implicit val writes2: Writes[ExLoanRow] = Json.writes[ExLoanRow]
  implicit val writes3: Writes[ThisResult] = Json.writes[ThisResult]
  implicit val writes4: Writes[LoanPayRow] = Json.writes[LoanPayRow]
  implicit val writes5: Writes[PayResult] = Json.writes[PayResult]
  implicit val reads1: Reads[LoanRow] = Json.reads[LoanRow]
  implicit val reads2: Reads[ExLoanRow] = Json.reads[ExLoanRow]
  implicit val reads3: Reads[LoanPayRow] = Json.reads[LoanPayRow]

  def index() = Action {
    Ok(views.html.loan())
  }

  def get(pg:Int, loan_number: String, bank_name: String, customer_id: Seq[String], min_money: Double, max_money: Double): Action[AnyContent] = Action.async {
    val res = loan.search(loan_number,bank_name,customer_id.filter(_.nonEmpty),min_money,if(max_money==0) Double.MaxValue else max_money)
    res.map {col=>
      Ok(ThisResult((col.size + page_items - 1) / page_items,col.slice(page_items*pg,page_items*(pg+1))).toJson)
    }
  }

  def getPay(loan_number:String): Action[AnyContent] = Action.async {
    loan.getPay(loan_number).map{res=>Ok(PayResult(res._2).toJson)}
  }

  def delete(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[ExLoanRow]
      loan.delete(req.loan.loanNumber) transformWith {
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
      val req = opt.as[ExLoanRow]
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
