package controllers

import java.sql.Date

import javax.inject.{Inject, Singleton}
import models.{AccountModel, BaseAccountRow}
import myutils.MyUtils.{page_items, parseDate}
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc._
import myutils.MyUtils._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class AccountController @Inject()(account: AccountModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  case class ThisResult(count:Int,rows:Seq[BaseAccountRow])
  implicit val writes1: Writes[BaseAccountRow] = Json.writes[BaseAccountRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]
  implicit val reads1: Reads[BaseAccountRow] = Json.reads[BaseAccountRow]

  // account_type: 0 for saving,1 for checking,2 for both
  def get(pg:Int,account_id:String,account_type:Int,bank_name:String,min_money:Double,max_money:Double,date_from:String,date_to:String): Action[AnyContent] = Action.async {
    val min_date = parseDate(date_from)
    val max_date = parseDate(date_to)
    val res = account.search(account_id,account_type,bank_name,min_money,max_money,min_date,if (max_date.getTime==0) new Date(Long.MaxValue) else max_date)
    res.map {col=>
      Ok(ThisResult(col.size,col.slice(page_items*pg,page_items*(pg+1))).toJson.toString)
    }
  }

  case class UpdateReq(old_row: BaseAccountRow, new_row: BaseAccountRow)

  implicit val reads: Reads[UpdateReq] = Json.reads[UpdateReq]

  def update(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[UpdateReq]
      account.update(req.old_row, req.new_row) transformWith {
        case Success(result) => Future(Ok(result.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse {
      Future(BadRequest("request error"))
    }
  }

  def delete(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[BaseAccountRow]
      account.delete(req.accountId) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse {
      Future(BadRequest("request error"))
    }
  }

  def insert(id_card:String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[BaseAccountRow]
      account.insert(id_card,req) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }

}
