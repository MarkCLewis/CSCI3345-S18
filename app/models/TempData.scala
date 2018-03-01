package models

case class DailyTemp(day: Int, doy: Int, month: Int, id: String,
    year: Int, precip: Double, tave: Double, tmax: Double, tmin: Double)

class TempData(filename: String) {
  private val data = scala.io.Source.fromFile(filename).getLines.drop(2).map { line =>
    val p = line.split(",")
    DailyTemp(p(0).toInt, p(1).toInt, p(2).toInt, p(3), p(4).toInt,
        p(5).toDouble, p(6).toDouble, p(7).toDouble, p(8).toDouble)
  }.toSeq
  
  def getMonth(month: Int, year: Int): Seq[DailyTemp] = {
    data.filter(dt => dt.month == month && dt.year == year)
  }

  def getRange(startMonth: Int, startYear: Int, endMonth: Int, endYear: Int): Seq[DailyTemp] = {
    data.filter(dt => 
      (dt.year > startYear || (dt.year == startYear && dt.month >= startMonth)) &&
      (dt.year < endYear || (dt.year == endYear && dt.month <= endMonth)))
  }

}