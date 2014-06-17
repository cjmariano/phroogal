package com.phroogal.web.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.phroogal.core.domain.User;
import com.phroogal.core.exception.ApplicationException;
import com.phroogal.core.resource.ApplicationMessage;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.web.bean.ResponseBean;

/**
 * Aspects that intercepts controller methods 
 * @author Christopher Mariano
 *
 */
@Aspect
@Component
public class ControllerAspect {
	
	private static final Logger log = Logger.getLogger(ControllerAspect.class);
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationService;

	@Around("execution(* com.phroogal.web.controller.service.*.*(..))")
	public Object executeControllerMethods(ProceedingJoinPoint jp) throws Throwable {
		Object obj = null;
		
		try {
			preProcessAction(jp);
			obj = jp.proceed();
			postProcessAction(jp, obj);
			if (obj instanceof ModelAndView) {
				return obj;
			} 
			return new ResponseBean(ResponseBean.Status.SUCCESS, obj);
			
		} catch (ApplicationException ae) {
			errorHandler(ae);
			ResponseBean errorResponse = new ResponseBean();
			return errorResponse.addErrorApplicationMessage(ae.getErrorMessage());
			
		} catch (Exception e) {
			User loggedinUser = authenticationService.getAuthenticatedUser();
			log.error(String.format("[%s]-%s", loggedinUser, e.getMessage()));
			log.error(e.getMessage());
			e.printStackTrace();
			return new ResponseBean(ResponseBean.Status.FAILURE, e.getMessage());
		}
	}

	private void preProcessAction(ProceedingJoinPoint jp) {
		log.info("Entering method: " + jp.getSignature().toShortString());
	}
	
	private void postProcessAction(ProceedingJoinPoint jp, Object returnObject) {
		log.info("Output: " + returnObject);
		log.info("Leaving method: " + jp.getSignature().toShortString());
	}
	
	private void errorHandler(ApplicationException ae) {
		User loggedinUser = authenticationService.getAuthenticatedUser();
		ApplicationMessage msg = ae.getErrorMessage();
		log.error(String.format("[%s][%s]-%s", loggedinUser, msg.getCode(), msg.getMessage()));
		if (ae.getThrowable() != null) {
			ae.getThrowable().printStackTrace();
		}
	}
}
