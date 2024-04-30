package com.emh.payload.response;

import org.springframework.beans.factory.annotation.Value;

public interface StudentSummaryResponse
{
	Integer getTestResultId();
	Integer getScore();
	Integer getStudentId();
	String getStudentName();
}