package com.emh.repos;

import com.emh.entity.StudentTestResult;
import com.emh.entity.Tests;
import com.emh.payload.response.StudentSummaryResponse;
import com.emh.payload.response.TestResultStatisticResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StudentTestResultRepository extends JpaRepository<StudentTestResult, Integer>, JpaSpecificationExecutor<StudentTestResult>
{

	StudentTestResult findFirstByTests(Tests tests);

	@Query(value = "SELECT s.student_id studentId, s.name studentName, str.score, str.id testResultId\n" +
			"FROM student s\n" +
			"LEFT JOIN (\n" +
			"        SELECT id, score, student_id, class_id, ROW_NUMBER() OVER (PARTITION BY student_id ORDER BY createddate DESC) AS newest\n" +
			"        FROM student_test_result\n" +
			"        WHERE class_id = ?1 AND test_code_id = ?2\n" +
			"    ) str \n" +
			"  ON s.student_id = str.student_id \n" +
			"     AND str.class_id = s.class_id \n" +
			"     AND str.newest = 1\n" +
			"WHERE s.class_id = ?1\n", nativeQuery = true)
	List<StudentSummaryResponse> statistic(int classId, int testId);

	@Query(value = "SELECT s.student_id studentId, s.name studentName, str.score, str.id testResultId\n" +
			"FROM student s\n" +
			"LEFT JOIN (\n" +
			"        SELECT id, score, student_id, class_id, ROW_NUMBER() OVER (PARTITION BY student_id ORDER BY createddate DESC) AS newest\n" +
			"        FROM student_test_result\n" +
			"        WHERE class_id = ?1 AND test_code_id = ?2\n" +
			"    ) str \n" +
			"  ON s.student_id = str.student_id \n" +
			"     AND str.class_id = s.class_id \n" +
			"     AND str.newest = 1\n" +
			"WHERE s.class_id = ?1  AND s.student_id = ?3\n", nativeQuery = true)
	List<StudentSummaryResponse> statistic(int classId, int testId, int studentId);
}
