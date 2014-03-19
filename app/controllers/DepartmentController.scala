package controllers

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}
import models.{Department, Student}
import views.html

object DepartmentController extends Controller {
  val departmentForm = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText(minLength = 5, maxLength = 45),
      "imageSrc" -> text(),
      "code" ->nonEmptyText(maxLength = 7)
    )(Department.apply)(Department.unapply)
  )

  def list = Action {
    implicit request =>
      Ok(html.department.list(Department.list()))
  }

  def save = Action {
    implicit request =>
      departmentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.department.createForm(formWithErrors)),
        department => {
          Department.insert(department)
          Ok(html.operationResult("Good"))
        }
      )
  }

  def create = Action {
    try {
      Ok(html.department.createForm(departmentForm))
    } catch {
      case e: Exception => sys.error(e.getMessage)
    }
  }

  def edit(id: Long) = Action {
    Department.findById(id).map {
      department =>
        Ok(html.department.editForm(id, departmentForm.fill(department)))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action {
    implicit request => {
      departmentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.department.editForm(id, formWithErrors)),
        department => {
          Department.update(id, department)
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
