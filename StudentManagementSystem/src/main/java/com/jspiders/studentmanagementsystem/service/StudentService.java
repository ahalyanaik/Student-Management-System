package com.jspiders.studentmanagementsystem.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jspiders.studentmanagementsystem.dto.MessageData;
import com.jspiders.studentmanagementsystem.dto.StudentRequest;
import com.jspiders.studentmanagementsystem.dto.StudentResponse;
import com.jspiders.studentmanagementsystem.util.ResponseStructure;

public interface StudentService {
	
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest);
	
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, int studentId);
	
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(int studentId);
	
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent(int studentId);
	
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents();
	
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail);
	
	public ResponseEntity<ResponseStructure<StudentResponse>> findByPhNo(String studentPhNo);
	
	public ResponseEntity<ResponseStructure<List<String>>> getStudentEmails(String grade);
	
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(MultipartFile file) throws IOException;
	
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException;
	
	public ResponseEntity<String> sendMail(MessageData messageData);
	
	public ResponseEntity<String> sendMimeMessage() throws MessagingException;
}

/**
 * Assignment --> read the data from the csv file and save it to the database
 * 
 * dependency ---> <groupId>org.apache.commons</groupId>
 * 					<artifactId>Commons-csv</artifactId>
 * 					<version> 1.8 </version>
 * 
 */
