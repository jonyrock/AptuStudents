package controllers

import play.api.mvc._
import views.html
import models.{Department, Student}

object Application extends Controller {

  def index = Action {
      Ok(html.index(Student.list(), Department.list()))
  }

}