package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Student
import views._
import anorm._

object Students extends Controller {

  val studentForm = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText(minLength = 5, maxLength = 40),
      "department" -> text
    )(Student.apply)(Student.unapply)
  )

  def list = Action {
    implicit request =>
      Ok(html.studentsList(Student.list))
  }

  def save = Action {
    implicit request =>
      studentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.studentCreateForm(formWithErrors)),
        student => {
          Student.insert(student)
          Ok(html.operationResult("Good"))
        }
      )
  }

  def create = Action {
    Ok(html.studentCreateForm(studentForm))
  }

  def edit(id: Long) = Action {
    Student.findById(id).map {
      student =>
        Ok(html.studentEditForm(id, studentForm.fill(student)))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action {
    implicit request => {
      studentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.studentEditForm(id, formWithErrors)),
        student => {
          Student.update(id, student)
          Ok(html.operationResult("Good"))
        }
      )
    }

  }

  def delete(id: Long) = TODO

}
