package com.jspiders.studentmanagementsystem.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // does not work for String type id
	//IDENTITY -> MySql take responsibility to create id, Each table will be having its own sequence
	//AUTO -> hibernate will take the responsibility to create id, database follows single sequence irrespective of different tables
	private int studentId;
	private String studentName;
	private String studentEmail;
	private long studentPhNo;
	private String studentGrade;
	private String studentPwd;
	public String getStudentPwd() {
		return studentPwd;
	}
	public void setStudentPwd(String studentPwd) {
		this.studentPwd = studentPwd;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentEmail() {
		return studentEmail;
	}
	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}
	public long getStudentPhNo() {
		return studentPhNo;
	}
	public void setStudentPhNo(long studentPhNo) {
		this.studentPhNo = studentPhNo;
	}
	public String getStudentGrade() {
		return studentGrade;
	}
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	
	
}
