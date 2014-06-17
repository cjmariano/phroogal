/**
 * 
 */
package com.phroogal.web.bean.mapper.impl;

import java.util.List;

import ma.glasnost.orika.MapperFacade;

import org.springframework.stereotype.Service;

import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.web.bean.mapper.MapperService;

/**
 * Java bean to Java bean mapper implementation by Orika
 * @author Christopher Mariano
 *
 */
@Service
public class OrikaMapper<DOMAIN, DTO> implements MapperService<DOMAIN, DTO> {

	private MapperFacade mapper = new OrikaConfigurableMapper();
	
	@Override
	public DOMAIN toDomain(DTO bean, Class<DOMAIN> domainClass) {
		return mapper.map(bean, domainClass);
	}

	@Override
	public DTO toBean(DOMAIN domain, Class<DTO> beanClass) {
		return mapper.map(domain, beanClass);
	}

	@Override
	public List<DOMAIN> toDomain(List<DTO> beans, Class<DOMAIN> domainClass) {
		List<DOMAIN> domains = CollectionUtil.arrayList();
		for (DTO bean : beans) {
			domains.add(mapper.map(bean, domainClass));
		}
		return domains;
	}

	@Override
	public List<DTO> toBean(List<DOMAIN> domains, Class<DTO> beanClass) {
		List<DTO> beans = CollectionUtil.arrayList();
		for (DOMAIN domain : domains) {
			beans.add(mapper.map(domain, beanClass));
		}
		return beans;
	}
}
