package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action{
    Ok(views.html.index("Hi dude"))
  }
  
  def students = TODO

  def student(id: Int) = TODO

  // routes.Application.hello("Bob")
  def courses = TODO

  def course(id: Int) = TODO 

}