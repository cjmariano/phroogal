package com.phroogal.web.bean.mapper.impl;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import com.phroogal.core.domain.GenderType;

public class OrikaGenderTypeConverter extends BidirectionalConverter<String, GenderType>{

	@Override
	public String convertFrom(GenderType genderType, Type<String> beanVal) {
		return genderType.getValue();
	}

	@Override
	public GenderType convertTo(String beanVal, Type<GenderType> genderType) {
		return GenderType.get(beanVal);
	}
}
