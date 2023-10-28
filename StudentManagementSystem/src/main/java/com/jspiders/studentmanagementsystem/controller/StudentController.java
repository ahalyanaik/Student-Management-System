package com.jspiders.studentmanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jspiders.studentmanagementsystem.dto.MessageData;
import com.jspiders.studentmanagementsystem.dto.StudentRequest;
import com.jspiders.studentmanagementsystem.dto.StudentResponse;
import com.jspiders.studentmanagementsystem.service.StudentService;
import com.jspiders.studentmanagementsystem.util.ResponseStructure;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService service;
	
	@PostMapping()
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentResponse){
		return service.saveStudent(studentResponse);
	}
	
	@PutMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody StudentRequest studentRequest, @PathVariable int studentId){ // @requestParam can be used instead of @pathVariable but end point of url changes
		return service.updateStudent(studentRequest, studentId);
	}
	
	@DeleteMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(@PathVariable int studentId){
		return service.deleteStudent(studentId);
	}
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents(){
		return service.findAllStudents();
	}
	
	@GetMapping(params="studentId")	 // @RequestParam is used bcz of ambiguity problem between the same mappings(GetMapping) bcz url converts the data in the form of String
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent(@RequestParam int studentId){
		return service.findStudent(studentId);
	}
	
	@GetMapping(params="studentEmail")
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByEmail(@RequestParam String studentEmail){
		return service.findByEmail(studentEmail);
	}
	
	@GetMapping(params="studentPhNo")	//@GetMapping("/{studentPhNo}") -> @PathVariable	
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudentByPhNo(@RequestParam String studentPhNo){
		return service.findByPhNo(studentPhNo);
	}
	
	@GetMapping("/{grade}")
	public ResponseEntity<ResponseStructure<List<String>>> getEmailsByGrade(@PathVariable String grade){
		return service.getStudentEmails(grade);
	}
	
	@PostMapping("/extract")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(@RequestParam MultipartFile file) throws IOException
	{
		return service.extractDataFromExcel(file);
	}
	
	@GetMapping("/writetoexcel")	// can give @PostMapping also
	public ResponseEntity<String> writeToExcel(@RequestParam String filePath) throws IOException{
	return service.writeToExcel(filePath);
	}
	
	@PostMapping("/sendmail")
	public ResponseEntity<String> sendMails(@RequestBody MessageData messageData){
		return service.sendMail(messageData);
	}
	
	@PostMapping("/mime-message")
	public ResponseEntity<String> sendMimeMessage()throws MessagingException{
		return service.sendMimeMessage();
	}
}

/**
 * findById
 * users/{userId} => /users/1234
 * Delete
 * users/{userId}/delete => /users/1234/delete
 * Update
 * users/{userId}/update/ => /users/1234/update
 * GetEmailByGrade
 * userGrade/{userGrade}/users/email => userGrade/A/users/email
 * 
 * encoding values for : and / in system resources
 * : -> %3A and / -> %2F
 * grvuwgkkcbqzljig
 */
