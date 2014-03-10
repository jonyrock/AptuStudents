package controllers

import play.api.mvc._
import views.html
import models.Student

object Application extends Controller {

  def index = Action {
      Ok(html.studentsList(Student.list))
  }

}