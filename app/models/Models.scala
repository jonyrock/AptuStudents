package models

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

case class Student(
                    id: Pk[Long] = NotAssigned,
                    name: String,
                    department: Long
                    )

case class Department(
                       id: Pk[Long] = NotAssigned,
                       name: String,
                       imageSrc: String,
                       code: String
                       )

object Student {

  val simple = {
    get[Pk[Long]]("student.id") ~
      get[String]("student.name") ~
      get[Long]("student.department") map {
      case id ~ name ~ department => Student(id, name, department)
    }
  }

  val withDepartment = Student.simple ~ (Department.simple ?) map {
    case computer ~ company => (computer, company)
  }

  def findById(id: Long): Option[Student] = {
    DB.withConnection(implicit connection =>
      SQL("select * from student where id = {id}")
        .on('id -> id)
        .as(Student.simple.singleOpt)
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

object Department {

  val simple = get[Pk[Long]]("department.id") ~
    get[String]("department.name") ~
    get[String]("department.imageSrc") ~
    get[String]("department.code") map {
    case id ~ name ~ imageSrc ~ code => Department(id, name, imageSrc, code)
  }

  def findById(id: Long): Option[Department] = {
    DB.withConnection(implicit connection =>
      SQL("select * from department where id = {id}")
        .on('id -> id)
        .as(Department.simple.singleOpt)
    )
  }

  def list() = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            select * from department
            order by name
          """).as(Department.simple *)
    }
  }

  def options() = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            select * from department
            order by name
          """)
          .as(Department.simple *)
          .map(c => c.id.toString -> c.name)
    }
  }

  def update(id: Long, department: Department) = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            update department
            set name = {name}, imageSrc = {imageSrc}
            where id = {id}
          """)
          .on(
            'id -> id,
            'name -> department.name,
            'imageSrc -> department.imageSrc
          ).executeUpdate()
    }
  }

  def insert(department: Department) = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            insert into department (name, department)
            values ({name}, {department})
          """)
          .on(
            'name -> department.name,
            'department -> department.imageSrc
          ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection {
      implicit connection =>
        SQL("delete from department where id = {id}").on('id -> id).executeUpdate()
    }
  }

}