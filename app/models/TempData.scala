package models

case class DailyTemp(day: Int, doy: Int, month: Int, id: String,
    year: Int, precip: Double, tave: Double, tmax: Double, tmin: Double)

class TempData(filename: String) {
  val data = scala.io.Source.fromFile(filename).getLines.drop(2).map { line =>
    val p = line.split(",")
    DailyTemp(p(0).toInt, p(1).toInt, p(2).toInt, p(3), p(4).toInt,
        p(5).toDouble, p(6).toDouble, p(7).toDouble, p(8).toDouble)
  }.toSeq
}