package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.{Department, Student}
import views._
import anorm._
import play.api.libs.json.Json.toJson
import play.api.libs.json.JsValue

object StudentController extends Controller {

  val studentForm = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "name" -> nonEmptyText(minLength = 5, maxLength = 45),
      "department" -> longNumber
    )(Student.apply)(Student.unapply)
  )

  def acc(t: String)(implicit request: Request[AnyContent]): Boolean =
    request.accepts("application/" + t) || request.accepts("text/" + t)

  def studentToJson(student: Student) = {
    toJson(
      Map(
        "id" -> toJson(student.id.get),
        "name" -> toJson(student.name)
      )
    )
  }

  def studentToJson(std: (Student, Department)) = {
    val (student, department) = std
    toJson(
      Map(
        "id" -> toJson(student.id.get),
        "name" -> toJson(student.name),
        "department" -> toJson(department.name)
      )
    )
  }

  def jsonToStudent(js: JsValue): Option[Student] = {

    var id: Long = 0
    var name: String = ""
    var department: Long = -1

    (js \ "id").asOpt[Long] match {
      case Some(id_) => id = id_
      case _ => ""
    }

    (js \ "name").asOpt[String] match {
      case Some(name_) => name = name_
      case _ => ""
    }

    (js \ "department").asOpt[Long] match {
      case Some(department_) => department = department_
      case _ => ""
    }

    if (name.equals("") || department == -1)
      None
    else
      Some(new Student(anorm.Id(id), name, department))

  }

  def list = Action {
    implicit request =>
      if (acc("html")) {
        listHtml
      } else if (acc("json")) {
        listJson
      } else {
        Status(488)("Strange accept type")
      }
  }

  def listHtml = Ok(html.student.list(Student.list()))

  def listJson = {
    Ok(toJson(Student.list().map(studentToJson)))
  }

  def saveHtml()(implicit request: Request[AnyContent]) = {
    studentForm.bindFromRequest.fold(
      formWithErrors => "bad",
      student => {
        Student.insert(student)
        "good"
      }
    )
  }

  def saveHtmlRes(res: String) = {
    res match {
      case "bad" => BadRequest(html.student.createForm(studentForm, Department.options()))
      case "good" => Ok(html.operationResult(res))
    }
  }

  def saveJson()(implicit request: Request[AnyContent]) = {
    jsonToStudent(request.body.asJson.get) match {
      case Some(student) =>
        Student.insert(student)
        "good"
      case _ => "bad"
    }
  }

  def saveJsonRes(res: String) = {
    Ok(toJson(Map("result" -> toJson(res))))
  }

  def save = Action {
    implicit request =>
      val res = request.contentType match {
        case Some("text/html") => saveHtml()
        case Some("application/json") => saveJson()
        case _ => ""
      }


      if (acc("html")) {
        saveHtmlRes(res)
      } else if (acc("json")) {
        saveJsonRes(res)
      } else {
        Status(488)("Strange accept type")
      }
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
