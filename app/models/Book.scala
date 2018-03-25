package models

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import controllers.NewBook

case class Book(title: String, isbn: String, price: Double)

/**
 * Object that I can put some queries in.
 */
object BookQueries {
  import Tables._
  def allBooks(db: Database)(implicit ec: ExecutionContext):Future[Seq[Book]] = {
    db.run(books.result)
  }
  
  def addBook(nb: NewBook, db: Database)(implicit ec: ExecutionContext): Future[Int] = {
    db.run {
      books += Book(nb.title, nb.isbn, nb.price.doubleValue())
    }
  }
}