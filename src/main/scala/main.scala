import slick.driver.PostgresDriver.api._
import slick.driver.PostgresDriver.QueryBuilder

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ILike {
  implicit class IlikeOps(s: Rep[String]) {
    def ilike(p: Rep[String]): Rep[Boolean] = {
			val expr = SimpleExpression.binary[String,String,Boolean] { (s, p, qb) =>
				qb.expr(s)
				qb.sqlBuilder += " ILIKE "
				qb.expr(p)
			}
			expr.apply(s,p)
		}
	}
}

object Example extends App {

  final case class Message(
    sender:  String,
    content: Option[String],
    id:      Long = 0L)

  def freshTestData = Seq(
    Message("Dave", Some("Hello, HAL. Do you read me, HAL?")),
    Message("HAL",  Some("Affirmative, Dave. I read you.")),
    Message("Dave", Some("Open the pod bay doors, HAL.")),
    Message("HAL",  Some("I'm sorry, Dave. I'm afraid I can't do that.")),
    Message("Dave", None)
  )

  final class MessageTable(tag: Tag)
      extends Table[Message](tag, "message") {

    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def sender  = column[String]("sender")
    def content = column[Option[String]]("content")

    def * = (sender, content, id) <> (Message.tupled, Message.unapply)
  }

  lazy val messages = TableQuery[MessageTable]
  
	import ILike._

  val program = for {
    _       <- messages.schema.create
    _       <- messages ++= freshTestData
    senders <- messages.filter(m => m.sender ilike "%dave%").result
  } yield senders

  val db = Database.forConfig("example")
  try {
    println(
        Await.result(db.run(program), 2 seconds)
      )
  } finally db.close

}
