package controllers

import dao.SubbranchRow
import javax.inject._
import models.SubbranchModel
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubbranchController @Inject()(subbranch:SubbranchModel, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  import subbranch._

  val page_items = 10

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.subbranch())
  }

  case class ThisResult(count:Int,rows:Seq[SubbranchRow])
  implicit val writes1:Writes[SubbranchRow] = Json.writes[SubbranchRow]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]

  def getPage(n:Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val jsBody = request.body.asJson
    jsBody.map { json =>
      val req = json.as[SubbranchSearch]
      val res = subbranch.search(req)
      res.map {col=>
        Ok(ThisResult((col.size+page_items-1)/page_items,col.slice(n*page_items,(n+1)*page_items)).toJson )
      }
    }.getOrElse {
      Future(BadRequest(""))
    }
    
  }
}

