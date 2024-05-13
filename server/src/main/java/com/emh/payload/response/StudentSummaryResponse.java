package com.emh.payload.response;

import com.emh.entity.Student;
import com.emh.entity.StudentTestResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StudentSummaryResponse
{
	private StudentInfo student;
	private Result result;

	@Getter
	@Setter
	public static class Result {
		Integer id;
		Double score;
		Double testDefaultScore;
		Boolean requiresGrading;

		@JsonSerialize(as = Date.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		@JsonProperty("created")
		Date createdDate;

		public Result() { }

		public Result(StudentTestResult studentTestResult) {
			this.id = studentTestResult.getId();
			this.testDefaultScore = studentTestResult.getTestDefaultScore();
			this.score = studentTestResult.getScore();
			this.requiresGrading = studentTestResult.getRequiresGrading();
			this.createdDate = studentTestResult.getCreatedDate();
		}
	};

	@Getter
	@Setter
	public static class StudentInfo
	{
		Integer studentId;
		String name;
		String email;
		Integer gender;
		String avatar;
		String username;

		public StudentInfo() { }

		public StudentInfo(Student student) {
			this.studentId = student.getStudentId();
			this.name = student.getName();
			this.email = student.getEmail();
			this.gender = student.getGender();
			this.avatar = student.getAvatar();
			this.username = student.getUsername();
		}
	};
}
