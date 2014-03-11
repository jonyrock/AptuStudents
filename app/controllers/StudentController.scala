package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.{Department, Student}
import views._
import anorm._

object StudentController extends Controller {

  val studentForm = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText(minLength = 5, maxLength = 45),
      "department" -> longNumber
    )(Student.apply)(Student.unapply)
  )

  def list = Action {
    implicit request =>
      Ok(html.student.list(Student.list))
  }

  def save = Action {
    implicit request =>
      studentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.student.createForm(formWithErrors, Department.options())),
        student => {
          Student.insert(student)
          Ok(html.operationResult("Good"))
        }
      )
  }

  def create = Action {
    try {
      Ok(html.student.createForm(studentForm, Department.options()))
    } catch {
      case e: Exception => sys.error(e.getMessage)
    }
  }

  def edit(id: Long) = Action {
    Student.findById(id).map {
      student =>
        Ok(html.student.editForm(id, studentForm.fill(student), Department.options()))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action {
    implicit request => {
      studentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.student.editForm(id, formWithErrors, Department.options())),
        student => {
          Student.update(id, student)
          Ok(html.operationResult("Good"))
        }
      )
    }
  }

  def delete(id: Long) = Action {
    implicit request => {
      Student.delete(id)
      Ok(html.operationResult("Good"))
    }
  }

}
