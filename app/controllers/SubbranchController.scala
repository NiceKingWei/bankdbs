package controllers

import myutils.MyUtils._
import dao.SubbranchRow
import javax.inject._
import models.SubbranchModel
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent._
import scala.util.{Failure, Success, Try}


@Singleton
class SubbranchController @Inject()(subbranch: SubbranchModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  import subbranch._


  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.subbranch())
  }

  case class ThisResult(count: Int, rows: Seq[SubbranchRow])

  implicit val reads1: Reads[SubbranchRow] = Json.reads[SubbranchRow]
  implicit val writes1: Writes[SubbranchRow] = Json.writes[SubbranchRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]

  def get(pg: Int, name: String, city: String, min: Double, max: Double): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val res = subbranch.search(name, city, min, max)
    res.map { col =>
      Ok(ThisResult((col.size + page_items - 1) / page_items, col.slice(pg * page_items, (pg + 1) * page_items)).toJson)
    }
  }

  case class UpdateReq(old_row: SubbranchRow, new_row: SubbranchRow)

  implicit val reads: Reads[UpdateReq] = Json.reads[UpdateReq]

  def update(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body = request.body.asJson
    body.map { opt =>
      val req = opt.as[UpdateReq]
      subbranch.update(req.old_row, req.new_row) transformWith {
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
      val req = opt.as[SubbranchRow]
      subbranch.delete(req.bankName) transformWith {
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
      val req = opt.as[SubbranchRow]
      subbranch.insert(req) transformWith {
        case Success(r) => Future(Ok(r.toString))
        case Failure(_) => Future(BadRequest("data error"))
      }
    }.getOrElse(Future(BadRequest("request error")))
  }
}

