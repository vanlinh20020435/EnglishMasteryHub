package com.emh.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpecificationsBuilder<T>
{
	private final SpecificationFactory<T> specificationFactory = new SpecificationFactory<>();

	private List<SearchCriteria> params = new LinkedList<>();

	public SpecificationsBuilder<T> with(SearchCriteria criteria) throws Exception
	{
		params.add(criteria);
		return this;
	}

	public Specification<T> build() throws Exception
	{
		if (params.isEmpty())
			return Specification.where(null);

		List<Specification<T>> specs = new ArrayList<>();
		for (SearchCriteria param : params)
		{
			Specification<T> specification = specificationFactory.getByCriteria(param);
			specs.add(specification);
		}

		Specification<T> result = specs.get(0);

		for (int i = 1; i < params.size(); i++)
		{
			result = params.get(i).isOrPredicate() ?
					Specification.where(result).or(specs.get(i)) :
					Specification.where(result).and(specs.get(i));
		}
		return result;
	}
}