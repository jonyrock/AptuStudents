package models

import anorm._


case class Student(
                    id: Pk[String] = NotAssigned,
                    name: String,
                    department: String
                    )

case class Course(
                   id: Pk[String] = NotAssigned,
                   name: String,
                   description: String
                   )


object Student {

  def findById(id: String) = {

  }

  def list() = {

  }

  def update(id: String, student: Student) = {

  }

  def insert(student: Student) = {

  }

  def delete(id: String) = {

  }

}

object Course {

  def findById(id: String) = {

  }

  def list() = {

  }

  def update(id: String, student: Student) = {

  }

  def insert(student: Student) = {

  }

  def delete(id: String) = {

  }

}