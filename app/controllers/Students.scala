package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.Student
import views._

object Students extends Controller {
  
  val studentForm = Form(
    mapping(
      "name"-> nonEmptyText(minLength = 5, maxLength = 40),
      "department"-> text
    )(Student.apply)(Student.unapply)
  )
  
  def list = TODO
  
  def create = Action { request =>
    Ok(html.createResult("Success"))
  }

  def createForm = Action {
    Ok(html.createForm(studentForm))
  }
  
  def show(id: Int) = TODO
  
  def update(id: Int) = TODO
  
  def delete(id: Int) = TODO
  
}
