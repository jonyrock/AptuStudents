package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Student(
                    id: Pk[Long] = NotAssigned,
                    name: String,
                    department: String
                    )

case class Course(
                   id: Pk[String] = NotAssigned,
                   name: String,
                   description: String
                   )


object Student {

  val simple = {
    get[Pk[Long]]("student.id") ~
      get[String]("student.name") ~
      get[String]("student.department") map {
      case id ~ name ~ department => Student(id, name, department)
    }
  }

  def findById(id: Long): Option[Student] = {
    DB.withConnection(implicit connection =>
      SQL("select * from student where id = {id}").on('id -> id).as(Student.simple.singleOpt)
    )
  }

  def list() = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
        select * from student
        order by student.name
          """).as(Student.simple *)
    }
  }

  def update(id: Long, student: Student) = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
        update student
        set name = {name}, department = {department}
        where id = {id}
          """)
          .on(
            'id -> id,
            'name -> student.name,
            'department -> student.department
          ).executeUpdate()
    }
  }

  def insert(student: Student) = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            insert into student (name, department)
            values ({name}, {department})
          """)
          .on(
            'name -> student.name,
            'department -> student.department
          ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection {
      implicit connection =>
        SQL("delete from student where id = {id}").on('id -> id).executeUpdate()
    }
  }

}

object Course {

  def findById(id: Long) = {

  }

  def list() = {

  }

  def update(id: Long, student: Student) = {

  }

  def insert(student: Student) = {

  }

  def delete(id: Long) = {

  }

}