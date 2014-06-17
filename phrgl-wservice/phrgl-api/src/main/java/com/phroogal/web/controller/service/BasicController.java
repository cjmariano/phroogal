/**
 * 
 */
package com.phroogal.web.controller.service;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.domain.PropertyModifiable;
import com.phroogal.core.service.Service;
import com.phroogal.core.valueobjects.PropertyBag;
import com.phroogal.web.bean.PageBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.phroogal.web.controller.rest.RestPatchOperationType;
import com.phroogal.web.controller.rest.RestPatchRequest;


/**
 * Parent class for controllers that provides basic functionalities
 * @author Christopher Mariano
 *
 */
public abstract class BasicController<DOMAIN extends Persistent<ID>, BEAN, ID extends Serializable> {
	
	@Autowired
	private MapperService<DOMAIN, BEAN> objectMapper;
	
	@Autowired
	private MapperService<PropertyBag, RestPatchRequest> propertyValueMapper;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private MapperService<Page<DOMAIN>, PageBean> pageRequestMapper;
	
	private Class<DOMAIN> domainClazz = null;
	
	private Class<BEAN> beanClazz = null;
	
	private Service<DOMAIN, ID> service = null;
	
	protected abstract Service<DOMAIN, ID> returnDomainService();
	
	@SuppressWarnings("unchecked")
	public BasicController() {
		if(getClass().getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
			domainClazz = (Class<DOMAIN>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
			beanClazz = (Class<BEAN>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
			service = returnDomainService();
		}
	}
	
	protected Object addUpdateResource(HttpServletRequest request, HttpServletResponse response, BEAN bean) {
		resolveDomainService();
		DOMAIN domain = objectMapper.toDomain(bean, domainClazz);
		service.saveOrUpdate(domain);
		return objectMapper.toBean(domain, beanClazz);
	}
	
	protected Object addUpdateResourceList(HttpServletRequest request, HttpServletResponse response, List<BEAN> beans) {
		resolveDomainService();
		List<DOMAIN> domain = objectMapper.toDomain(beans, domainClazz);
		service.saveOrUpdate(domain);
		return objectMapper.toBean(domain, beanClazz);
	}
	
	@SuppressWarnings("unchecked")
	//TODO : modify return object that includes status for each patch
	protected Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, List<RestPatchRequest> patchRequestList) throws Exception {
		resolveDomainService();
		DOMAIN domain = service.findById((ID) id);
		if (domain instanceof PropertyModifiable) {
			BEAN bean = beanClazz.newInstance();
			for (RestPatchRequest patchRequest : patchRequestList) {
				RestPatchOperationType operation = patchRequest.getOperation();
				if (operation != null && operation.isSupported()) {
					PropertyBag propertyBag = createPropertyBag(bean, patchRequest);
					operation.execute((PropertyModifiable) domain, propertyBag);	
				}
			}	
			service.saveOrUpdate(domain);
			return objectMapper.toBean(domain, beanClazz);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Object getResource(@PathVariable Object id, HttpServletRequest request, HttpServletResponse response) {
		resolveDomainService();
		DOMAIN domain = service.findById((ID) id);
		return objectMapper.toBean(domain, beanClazz);
	}
	
	@SuppressWarnings("unchecked")
	public void deleteResource(@PathVariable Object id, HttpServletRequest request, HttpServletResponse response) {
		resolveDomainService();
		DOMAIN domain = service.findById((ID) id);
		service.delete(domain);
	}
	
	public Object getAllResources(HttpServletRequest request, HttpServletResponse response) {
		resolveDomainService();
		List<DOMAIN> domainAll = service.findAll();
		return objectMapper.toBean(domainAll, beanClazz);
	}
	
	public Object getAllResources( int pageAt, int pageSize, HttpServletRequest request, HttpServletResponse response) {
		resolveDomainService();
		Page<DOMAIN> pageRequest = service.findAll(pageAt, pageSize);
		return getPaginatedResults(pageRequest, beanClazz);
	}	
	
	@SuppressWarnings("unchecked")
	protected PageBean<BEAN> getPaginatedResults(Page<DOMAIN> pageRequest, Class<BEAN> beanClazz) {
		PageBean<BEAN> pageBean = pageRequestMapper.toBean(pageRequest, PageBean.class);
		pageBean.setContent(objectMapper.toBean(pageRequest.getContent(), beanClazz));
		return pageBean;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected PageBean getPaginatedResults(Page pageRequest, Class beanClazz, MapperService mapper) {
		PageBean pageBean = pageRequestMapper.toBean(pageRequest, PageBean.class);
		pageBean.setContent(mapper.toBean(pageRequest.getContent(), beanClazz));
		return pageBean;
	}
	
	protected MapperService<DOMAIN, BEAN> getObjectMapper() {
		return objectMapper;
	}
	
	private PropertyBag createPropertyBag(BEAN bean, RestPatchRequest patchRequest) throws Exception {
		PropertyUtils.setProperty(bean, patchRequest.getProperty(), patchRequest.getValue());
		DOMAIN domain = objectMapper.toDomain(bean, domainClazz);
		
		PropertyBag propertyBag = propertyValueMapper.toDomain(patchRequest, PropertyBag.class);
		propertyBag.setValue(PropertyUtils.getProperty(domain, patchRequest.getProperty()));
		return propertyBag;
	}
	
	private void resolveDomainService() {
		if (service == null) {
			service = returnDomainService();
		}
	}
}
