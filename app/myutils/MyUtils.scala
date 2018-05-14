package myutils

/**
  * Json support
  */
import java.sql.Date
import play.api.libs.json._

object MyUtils{
  implicit class ToJson[A](x:A)(implicit wft:Writes[A]) {
    val toJson: JsValue = Json.toJson(x)
  }
  val page_items = 10

  def parseDate(d:String): Date = {
    val s = d.split("-")    // yyyy-mm-dd
    if(s.length==2){
      new Date(s(0).toInt,s(1).toInt,s(2).toInt)
    } else {
      new Date(0)
    }
  }
}
