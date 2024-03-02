package com.emh.specifications;

import lombok.Data;

@Data
public class SearchCriteria
{

	private String key;
	private FilterOperation operation;
	private Object value;
	private Boolean isOrPredicate;

	public SearchCriteria(String key, String operation, Object value, Boolean isOrPredicate) throws Exception
	{
		this.key = key;
		this.operation = FilterOperation.fromValue(operation);
		this.value = value;
		this.isOrPredicate = isOrPredicate;
	}

	public boolean isOrPredicate()
	{
		return isOrPredicate;
	}

}