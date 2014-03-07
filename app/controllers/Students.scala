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
      "id" -> ignored(NotAssigned:Pk[String]),
      "name"-> nonEmptyText(minLength = 5, maxLength = 40),
      "department"-> text
    )(Student.apply)(Student.unapply)
  )
  
  def list = TODO
  
  def save = Action { implicit request =>
      studentForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors)),
      student => {
        Student.insert(student)
        Ok(html.createResult("Good"))
      }
    )
  }

  def createForm = Action {
    Ok(html.createForm(studentForm))
  }
  
  def show(id: Int) = TODO
  
  def update(id: Int) = TODO
  
  def delete(id: Int) = TODO
  
}
