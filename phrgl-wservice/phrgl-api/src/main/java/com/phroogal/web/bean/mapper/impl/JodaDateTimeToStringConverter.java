package com.phroogal.web.bean.mapper.impl;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaDateTimeToStringConverter extends BidirectionalConverter<String, DateTime>{

	/**
	 * Date time pattern : 03-28-2014
	 */
	private static final String pattern = "MM-dd-yyyy";
	
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
	
	@Override
	public DateTime convertTo(String source, Type<DateTime> beanVal) {
		return formatter.parseDateTime(source);
	}

	@Override
	public String convertFrom(DateTime source, Type<String> destinationType) {
		return source.toString(formatter);
	}
}
