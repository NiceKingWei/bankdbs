package controllers

import dao.SubbranchRow
import javax.inject._
import models.SubbranchModel
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext

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

  def getPage(pg:Int,name:String,city:String,min:Double,max:Double): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val res = subbranch.search(SubbranchSearch(name,city,min,max))
    res.map {col=>
      Ok(ThisResult((col.size+page_items-1)/page_items,col.slice(pg*page_items,(pg+1)*page_items)).toJson )
    }
  }
}

