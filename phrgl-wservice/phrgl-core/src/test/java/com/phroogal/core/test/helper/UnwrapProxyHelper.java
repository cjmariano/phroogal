/**
 * 
 */
package com.phroogal.core.test.helper;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * Provides convenience classes for unwrapping proxies
 * @author Christopher Mariano
 *
 */
public final class UnwrapProxyHelper {

	/**
	 * Use this method to unwrap an object which is proxied. Sometimes this is necessary to expose
	 * the actual attributes by the original class being proxied.
	 * @param clazz is the actual class that is being unwrapped
	 * @param proxy - wrapper proxy class
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object unwrapProxy(Class clazz, Object proxy) {
		 if(AopUtils.isAopProxy(proxy) && proxy instanceof Advised) {
			 try{
				 Object target = ((Advised)proxy).getTargetSource().getTarget();
				 return target;
			 }catch (Exception e) {}
		  }
		 return proxy;
	}
}
