package com.jspiders.studentmanagementsystem.serviceimpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jspiders.studentmanagementsystem.dto.MessageData;
import com.jspiders.studentmanagementsystem.dto.StudentRequest;
import com.jspiders.studentmanagementsystem.dto.StudentResponse;
import com.jspiders.studentmanagementsystem.entity.Student;
import com.jspiders.studentmanagementsystem.exception.StudentNotFoundByEmailException;
import com.jspiders.studentmanagementsystem.exception.StudentNotFoundByIdException;
import com.jspiders.studentmanagementsystem.exception.StudentNotFoundByPhNoException;
import com.jspiders.studentmanagementsystem.repository.StudentRepo;
import com.jspiders.studentmanagementsystem.service.StudentService;
import com.jspiders.studentmanagementsystem.util.ResponseStructure;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private JavaMailSender javaMailsender;
	
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {
		Student student = new Student();
		// setting student object with requested data(dto-StudentRequest)
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPhNo(studentRequest.getStudentPhNo());
		student.setStudentPwd(studentRequest.getStudentPwd());

		Student student2 = repo.save(student);

		// setting the response object with the student data(dto-StudentResponse)
		StudentResponse response = new StudentResponse();
		response.setStudentId(student2.getStudentId());
		response.setStudentName(student2.getStudentName());
		response.setStudentGrade(student2.getStudentGrade());

		// returning the response object(dto object)
		ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Student data saved successfully!!");
		structure.setData(response);
		return ResponseEntity.ok(structure); //--> this also works(ok is the static method which accepts only type of ResponseEntity)
//		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED) ;
	}

	//	@Override
	//	public ResponseEntity<Student> updateStudent(Student student, int studentId) {
	//		Optional<Student> findById = repo.findById(studentId);
	//		if(findById.isPresent()) {
	//			Student student2 = findById.get();
	//			student.setStudentId(studentId);
	//			Student student3 = repo.save(student);
	//			return new ResponseEntity<Student>(student3, HttpStatus.OK);
	//		}
	//		return null;
	//	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, int studentId) {
		Optional<Student> findById = repo.findById(studentId);
		if(findById.isPresent()) {
			Student student2 = findById.get();
			student2.setStudentName(studentRequest.getStudentName());
			student2.setStudentEmail(studentRequest.getStudentEmail());
			student2.setStudentGrade(studentRequest.getStudentGrade());
			student2.setStudentPhNo(studentRequest.getStudentPhNo());
			student2.setStudentPwd(studentRequest.getStudentPwd());
			Student student3 = repo.save(student2);

			// setting the response object with the student data(dto-StudentResponse)
			StudentResponse response = new StudentResponse();
			response.setStudentId(student2.getStudentId());
			response.setStudentName(student2.getStudentName());
			response.setStudentGrade(student2.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student data updated successfully!!");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		}
		else{
			throw new StudentNotFoundByIdException("Failed to update the Student data!!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(int studentId) {
		Optional<Student> student = repo.findById(studentId);
		if(student.isPresent()) {
			repo.deleteById(studentId);
			Student student1 = student.get();
			StudentResponse response = new StudentResponse();
			response.setStudentId(student1.getStudentId());
			response.setStudentName(student1.getStudentName());
			response.setStudentGrade(student1.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student data deleted successfully!!");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		}
		else {
			throw new StudentNotFoundByIdException(" Failed to Delete the student!!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent(int studentId) {
		Optional<Student> findById = repo.findById(studentId);
		if(findById.isPresent()) {
			Student student = findById.get();
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student data found successfully!!");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		else {
			throw new StudentNotFoundByIdException("Could not fetch Student");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() {
		List<Student> students = repo.findAll();
		if(!students.isEmpty()) {
			List<StudentResponse> responses = new ArrayList<StudentResponse>();
			for(Student student:students) {
				StudentResponse response = new StudentResponse();
				response.setStudentId(student.getStudentId());
				response.setStudentName(student.getStudentName());
				response.setStudentGrade(student.getStudentGrade());
				responses.add(response);
			}
			ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student data found successfully!!");
			structure.setData(responses);
			return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.FOUND);
		}
		else {
			throw new StudentNotFoundByIdException("Could not fetch the Students");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail) {
		Student student = repo.findByStudentEmail(studentEmail);
		if(student!=null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student data found successfully!!");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);

		}
		else {
			throw new StudentNotFoundByEmailException("Failed to find student");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByPhNo(String studentPhNo) {
		Student student = repo.findByStudentPhNo(studentPhNo);
		if(student!=null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student data found successfully");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		else {
			throw new StudentNotFoundByPhNoException("Failed to find the student");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<String>>> getStudentEmails(String grade) {
		List<String> studentEmails = repo.getAllEmailByGrade(grade);
		ResponseStructure<List<String>> structure = new ResponseStructure<List<String>>();
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("Student data found successfully");
		structure.setData(studentEmails);
		return new ResponseEntity<ResponseStructure<List<String>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(MultipartFile file) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		List<StudentResponse> responses = new ArrayList<StudentResponse>();
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		for(Sheet sheet:workbook) {
			for(Row row:sheet) {
				if(row.getRowNum()>0) {
					if(row!=null) {
						Student student = new Student();
						student.setStudentName(row.getCell(0).getStringCellValue());
						student.setStudentEmail(row.getCell(1).getStringCellValue());
						student.setStudentPhNo((long)row.getCell(2).getNumericCellValue());
						student.setStudentGrade(row.getCell(3).getStringCellValue());
						student.setStudentPwd(row.getCell(4).getStringCellValue());
						repo.save(student);
						
						StudentResponse response = new StudentResponse();
						response.setStudentId(student.getStudentId());
						response.setStudentName(student.getStudentName());
						response.setStudentGrade(student.getStudentGrade());
						responses.add(response);

						// returning the response object(dto object)
						
						structure.setStatus(HttpStatus.CREATED.value());
						structure.setMessage("Student data saved successfully!!");
						structure.setData(responses);
					}
				}
			}
		}
		workbook.close();
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException {
		List<Student> students = repo.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet();
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("StudentId");
		header.createCell(1).setCellValue("StudentName");
		header.createCell(2).setCellValue("StudentEmail");
		header.createCell(3).setCellValue("StudentPhNo");
		header.createCell(4).setCellValue("StudentGrade");
		header.createCell(5).setCellValue("StudentPassword");
		
		int rowNum = 1;
		for(Student student:students) {
			Row row = sheet.createRow(rowNum++);
			
			row.createCell(0).setCellValue(student.getStudentId());
			row.createCell(1).setCellValue(student.getStudentName());
			row.createCell(2).setCellValue(student.getStudentEmail());
			row.createCell(3).setCellValue(student.getStudentPhNo());
			row.createCell(4).setCellValue(student.getStudentGrade());
			row.createCell(5).setCellValue(student.getStudentPwd());
		}
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		workbook.close();
		return new ResponseEntity<String> ("Data transfered to Excel sheet!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText());
		message.setSentDate(new Date());
		
		javaMailsender.send(message);
		return new ResponseEntity<String>("Mail sent successfully!!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMimeMessage() throws MessagingException {
		
		List<Student> findAll = repo.findAll();
		if(!findAll.isEmpty()) {
			MimeMessage mime = javaMailsender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mime, true);
			MessageData messageData = new MessageData();
			messageData.setSubject("Student's results with grade");
			messageData.setSenderName("KLE Technological University");
			messageData.setSenderAddress("Vidyanagar, Hubballi-580021\nKarnataka");
			
			String[] studentEmail = new String[1];
			for(Student student:findAll) {
				studentEmail[0] = student.getStudentEmail();
				messageData.setTo(studentEmail);
				messageData.setText("Hi "+student.getStudentName()+", Your Grade is "+student.getStudentGrade());
				
				message.setTo(messageData.getTo());
				message.setSubject(messageData.getSubject());
				
				String emailBody = messageData.getText() + "<br> <br> <h4> Thanks & Regards <br>" 
						+messageData.getSenderName()+"<br>"+messageData.getSenderAddress()+"</h4> " + " <img src=\"https://upload.wikimedia.org/wikipedia/en/thumb/c/c9/KLE_Technological_University_Logo.png/350px-KLE_Technological_University_Logo.png\" width=\"\">";
						
				message.setText(emailBody, true);
				message.setSentDate(new Date());
				
				javaMailsender.send(mime);
			}
			return new ResponseEntity<String>("Mime Message sent successfully", HttpStatus.OK);
		}
		else {
			throw new StudentNotFoundByIdException("Could not fetch the Students");
		}
	}
}
