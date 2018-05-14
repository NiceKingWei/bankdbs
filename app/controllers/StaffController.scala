package controllers

import java.sql.Date

import dao.StaffRow
import javax.inject.{Inject, Singleton}
import models.StaffModel
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc._
import myutils.MyUtils._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class StaffController @Inject()(staff: StaffModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  case class ThisResult(count:Int,rows:Seq[StaffRow])
  implicit val writes1: Writes[StaffRow] = Json.writes[StaffRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]
  implicit val reads1: Reads[StaffRow] = Json.reads[StaffRow]


  def get(pg:Int,id:String,name:String,bankName:String,date_from:String,date_to:String): Action[AnyContent] = Action.async {
    val min_date = parseDate(date_from)
    val max_date = parseDate(date_to)
    val res = staff.search(id,name,bankName,min_date,
      if (max_date.getTime==0) new Date(Long.MaxValue) else max_date)
    res.map {col=>
      Ok(ThisResult(col.size,col.slice(page_items*pg,page_items*(pg+1))).toJson.toString)
    }
  }

  case class UpdateReq(old_row: StaffRow, new_row: StaffRow)

  implicit val reads: Reads[UpdateReq] = Json.reads[UpdateReq]

  def update(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[UpdateReq]
      staff.update(req.old_row, req.new_row) transformWith {
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
      val req = opt.as[StaffRow]
      staff.delete(req.bankName) transformWith {
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
      val req = opt.as[StaffRow]
      staff.insert(req) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }
  
}
