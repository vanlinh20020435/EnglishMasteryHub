package com.emh.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestResultStatisticResponse
{
	private long completed;
	private long incomplete;
	private List<StudentSummaryResponse> statistic;
}
