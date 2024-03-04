package com.emh.specifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchForeignCriteria extends SearchCriteria
{
	private String foreignName;

	public SearchForeignCriteria(String foreignName, String key, String operation, Object value, Boolean isOrPredicate) throws Exception
	{
		super(key, operation, value, isOrPredicate);
		this.foreignName = foreignName;
	}
}