/**
 * 
 */
package com.phroogal.web.bean.mapper.impl;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Allows for configurable settings to be applied to the Orika Mapper
 * 
 * @author Christopher Mariano
 * 
 */
public class OrikaConfigurableMapper extends ConfigurableMapper {
	
	@Override
	public void configure(MapperFactory mapperFactory) {
		ConverterFactory converterFactory = mapperFactory.getConverterFactory();
		converterFactory.registerConverter(new PassThroughConverter(org.joda.time.DateTime.class));
		converterFactory.registerConverter(new OrikaGenderTypeConverter());
		converterFactory.registerConverter(new JodaDateTimeToStringConverter());
	}

	@Override
	public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
		builder.mapNulls(false);
	}
}
