package com.emh.util;

import com.emh.model.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;

public class MapperUtils
{
	private static ModelMapper mapper;

	public static <D, T> List<D> mapList(final Collection<T> entityList, Class<D> outCLass)
	{
		if (mapper == null)
			mapper = new ModelMapper(MatchingStrategies.STANDARD);
		return mapper.mapList(entityList, outCLass);
	}

	public static <D> D map(Object source, Class<D> destinationType)
	{
		if (mapper == null)
			mapper = new ModelMapper(MatchingStrategies.STANDARD);
		return mapper.map(source, destinationType);
	}
}