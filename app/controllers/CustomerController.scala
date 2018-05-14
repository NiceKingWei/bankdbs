package controllers

import dao.CustomerRow
import javax.inject.{Inject, Singleton}
import models.CustomerModel
import myutils.MyUtils.page_items
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc._
import myutils.MyUtils._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CustomerController @Inject()(customer: CustomerModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  case class ThisResult(count:Int,rows:Seq[CustomerRow])
  implicit val writes1: Writes[CustomerRow] = Json.writes[CustomerRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]
  implicit val reads1: Reads[CustomerRow] = Json.reads[CustomerRow]

  def get(pg:Int,id_card:String,staff_id_card:String,name:String,contact_name:String): Action[AnyContent] = Action.async {
    val res = customer.search(id_card,staff_id_card,name,contact_name)
    res.map {col=>
      Ok(ThisResult(col.size,col.slice(page_items*pg,page_items*(pg+1))).toJson.toString)
    }
  }

  case class UpdateReq(old_row: CustomerRow, new_row: CustomerRow)

  implicit val reads: Reads[UpdateReq] = Json.reads[UpdateReq]

  def update(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[UpdateReq]
      customer.update(req.old_row, req.new_row) transformWith {
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
      val req = opt.as[CustomerRow]
      customer.delete(req.idCard) transformWith {
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
      val req = opt.as[CustomerRow]
      customer.insert(req) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }
}
