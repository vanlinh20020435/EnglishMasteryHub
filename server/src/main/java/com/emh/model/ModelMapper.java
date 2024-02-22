package com.emh.model;

import org.modelmapper.spi.MatchingStrategy;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
public class ModelMapper extends org.modelmapper.ModelMapper
{
	public ModelMapper()
	{
	}

	public ModelMapper(MatchingStrategy matchingStrategy)
	{
		getConfiguration().setMatchingStrategy(matchingStrategy);
	}

	public <D, T> List<D> mapList(final Collection<T> entityList, Class<D> outCLass) {
		return entityList.stream()
				.map(entity -> map(entity, outCLass))
				.collect(Collectors.toList());
	}
}
