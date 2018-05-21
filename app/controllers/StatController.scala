package controllers

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

import models.{StatModel, StatResult}
import myutils.MyUtils._
import javax.inject._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent._


@Singleton
class StatController @Inject()(stat:StatModel,cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def index() = Action {
    Ok(views.html.stat())    
  }

  import stat._

  case class ThisResult(rows:Seq[StatResult])
  implicit val reads1: Reads[StatResult] = Json.reads[StatResult]
  implicit val writes1: Writes[StatResult] = Json.writes[StatResult]
  implicit val writes2: Writes[ThisResult] = Json.writes[ThisResult]

  def get(is_saving:Boolean,period:String,year:String,season:String,month:String): Action[AnyContent] = Action.async{
    println(year.toInt,month.toInt,season)
    val ft = new SimpleDateFormat ("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    val (tMinDate,tMaxDate) = period match {
      case "按年" =>
        ((year.toInt,0,1),(year.toInt+1,0,1))
      case "按季度" =>
        season match {
          case "春" =>
            ((year.toInt,0,1),(year.toInt,3,1))
          case "夏" =>
            ((year.toInt,3,1),(year.toInt,6,1))
          case "秋" =>
            ((year.toInt,6,1),(year.toInt,9,1))
          case "冬" =>
            ((year.toInt,9,1),(year.toInt+1,0,1))
        }
      case "按月" =>
        val m = month.toInt-1
        if(m!=11){
          ((year.toInt,m,1),(year.toInt,m+1,1))
        }else{
          ((year.toInt,m,1),(year.toInt+1,1,1))
        }
    }
    calendar.set(tMinDate._1,tMinDate._2,tMinDate._3)
    val minDate = calendar.getTime.getTime
    calendar.set(tMaxDate._1,tMaxDate._2,tMaxDate._3)
    val maxDate = calendar.getTime.getTime
    stat.stat(is_saving,new Date(minDate),new Date(maxDate)).map(item=>Ok(ThisResult(item).toJson))
  }
}