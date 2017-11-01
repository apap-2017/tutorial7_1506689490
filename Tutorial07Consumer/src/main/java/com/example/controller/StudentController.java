package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dao.StudentMapper;
import com.example.model.CourseModel;
import com.example.model.StudentModel;
import com.example.service.StudentService;

@Controller
public class StudentController
{
    @Autowired
    StudentService studentDAO;


    @RequestMapping("/")
    public String index (Model model)
    {	
    	model.addAttribute("title", "Index");
        return "index";
    }


    @RequestMapping("/student/add")
    public String add (Model model)
    {
    	model.addAttribute("title", "Add Student");
        return "form-add";
    }
    
    


    @RequestMapping("/student/add/submit")
    public String addSubmit (
            @RequestParam(value = "npm", required = false) String npm,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "gpa", required = false) double gpa,
            Model model)
    {
        StudentModel student = new StudentModel (npm, name, gpa, null);
        studentDAO.addStudent (student);
        model.addAttribute("title", "Sukses Add Student");
        return "success-add";
    }
    
    @RequestMapping(value={"/student/update","/student/update/{npm}"})
    public String update(@PathVariable Optional<String> npm, Model model)
    {
    	if(!npm.isPresent()){
    		model.addAttribute("npm", null);
    		model.addAttribute("title", "Not found");
    		return "not-found";
    	}
    	
    	StudentModel student = studentDAO.selectStudent(npm.get());
    	if(student != null){
    		model.addAttribute("student", student);
    		model.addAttribute("title", "Update Student");
    		return "form-update";
    	}
    	model.addAttribute("npm", npm.get());
    	model.addAttribute("title", "Not found");
    	return "not-found";
  
    }
    
//    @RequestMapping(value="/student/update/submit", method = RequestMethod.POST)
//    public String updateSubmit(
//    		@RequestParam(value = "npm", required = false) String npm,
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "gpa", required = false) double gpa,
//            Model model)
//    {
//    	if(npm != null){
//    		StudentModel student = studentDAO.selectStudent(npm);
//    		if(student == null){
//    			model.addAttribute("npm", npm);
//    			return "not-found";
//    		}
//    		
//    		student = new StudentModel(npm, name, gpa);
//    		studentDAO.updateStudent(student);
//        	return "success-update";
//    	}
//    	
//    	model.addAttribute("npm", "not-supplied");
//		return "not-found";
//    	
//    }
    
    
    
    @RequestMapping(value="/student/update/submit", method = RequestMethod.POST)
    public String updateSubmit(
    		@Valid StudentModel student,
    		BindingResult bindingResult,
            Model model)
    {
    	
    	if(bindingResult.hasErrors()){
    		model.addAttribute("title", "Not found");
    		return "not-found";
    	}
    	
    	StudentModel studentTemp = studentDAO.selectStudent(student.getNpm());
    	if(studentTemp == null){
    		model.addAttribute("npm", student.getNpm());
    		model.addAttribute("title", "Not found");
    		return "not-found";
    	}
    		
    	studentDAO.updateStudent(student);
    	model.addAttribute("title", "Sukses Update Student");
        return "success-update";
    	
    	
    }
   
    


    @RequestMapping("/student/view")
    public String view (Model model,
            @RequestParam(value = "npm", required = false) String npm)
    {
        StudentModel student = studentDAO.selectStudent (npm);

        if (student != null) {
            model.addAttribute ("student", student);
            model.addAttribute("title", "Detail Student");
            return "view";
        } else {
        	model.addAttribute("title", "Not found");
            model.addAttribute ("npm", npm);
            return "not-found";
        }
    }


    @RequestMapping("/student/view/{npm}")
    public String viewPath (Model model,
            @PathVariable(value = "npm") String npm)
    {
        StudentModel student = studentDAO.selectStudent (npm);

        if (student != null) {
            model.addAttribute ("student", student);
            model.addAttribute("title", "Detail Student");
            return "view";
        } else {
            model.addAttribute ("npm", npm);
            model.addAttribute("title", "Not found");
            return "not-found";
        }
    }


    @RequestMapping("/student/viewall")
    public String view (Model model)
    {
        List<StudentModel> students = studentDAO.selectAllStudents ();
        model.addAttribute ("students", students);
        model.addAttribute("title", "All Students");
        return "viewall";
    }


    @RequestMapping(value={"/student/delete","/student/delete/{npm}"})
    public String delete (Model model, @PathVariable(value = "npm") Optional<String> npm)
    {
    	if(!npm.isPresent()){
    		model.addAttribute("npm", null);
    		model.addAttribute("title", "Not found");
    		return "not-found";
    	}
    	StudentModel student = studentDAO.selectStudent(npm.get());
    	
    	if(student != null){
    		 studentDAO.deleteStudent (npm.get());
    		 model.addAttribute("title", "Delete Student");
    		 return "delete";
    	}
    	
    	model.addAttribute("npm", npm.get());
    	model.addAttribute("title", "Not found");
    	return "not-found";
    }
    
    @RequestMapping("/course/viewall")
    public String courses(Model model){
    	List<CourseModel> courses = studentDAO.selectAllCourses();
    	model.addAttribute("courses", courses);
    	return "course-all";
    }
    
    @RequestMapping("/course/view/{id}")
    public String course(@PathVariable(value="id") String id, Model model){
    	
    	CourseModel course = studentDAO.selectCourse(id);
    	model.addAttribute("course", course);
    	model.addAttribute("title", "Detail Course");
    	return "course";
    }

}
