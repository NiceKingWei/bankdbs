package myutils

/**
  * Json support
  */
import java.sql.Date
import java.text.SimpleDateFormat

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

  implicit val date_writes: Writes[Date] = (o: Date) => {
    val ft = new SimpleDateFormat("yyyy-MM-dd")
    JsString(ft.format(new java.util.Date(o.getTime)))
  }

  def someNull(x:Option[String]): Option[String] = x.flatMap(y=>if (y.isEmpty) None else Some(y))
//  implicit val date_reads: Reads[Date] = (js: JsValue) => {
//    val ft = new SimpleDateFormat("yyyy-mm-dd")
//    JsString(ft.format(new java.util.Date(o.getTime)))
//  }
}
