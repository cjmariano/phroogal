package com.phroogal.web.bean.mapper;

import java.util.List;


/**
 * Provides service to convert from domain to bean and vice versa
 * @author Christopher Mariano
 *
 * @param <DOMAIN>
 * @param <DTO>
 */
public interface MapperService<DOMAIN, DTO> {
	
	/**
	 * Converts bean to domain
	 * @param bean 
	 * @param domainClass
	 * @return an instance of the domain
	 */
	public DOMAIN toDomain(DTO bean, Class<DOMAIN> domainClass);
	
	/**
	 * Converts a list of bean to domain
	 * @param bean 
	 * @param domainClass
	 * @return an instance of the domain
	 */
	public List<DOMAIN> toDomain(List<DTO> beans, Class<DOMAIN> domainClass);
	
	/**
	 * Converts domain to bean
	 * @param domain
	 * @param beanClass 
	 * @return an instance of bean
	 */
	public DTO toBean(DOMAIN domain, Class<DTO> beanClass);
	
	/**
	 * Converts a list of domains to bean
	 * @param domain
	 * @param beanClass 
	 * @return an instance of bean
	 */
	public List<DTO> toBean(List<DOMAIN> domains, Class<DTO> beanClass);

}
