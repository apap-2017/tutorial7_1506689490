package com.example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.CourseModel;
import com.example.service.StudentService;

@RestController
@RequestMapping("/rest")
public class CourseRestController {
	@Autowired
	StudentService courseService;
	
	@RequestMapping("/course/view/{id_course}")
	public CourseModel course(@PathVariable(value = "id_course") String id_course){
		CourseModel course = courseService.selectCourse(id_course);
		return course;
	}
	
	@RequestMapping("/course/viewall")
	public List<CourseModel> courses(){
		List<CourseModel> courses = courseService.selectAllCourse();
		return courses;
	}
}
