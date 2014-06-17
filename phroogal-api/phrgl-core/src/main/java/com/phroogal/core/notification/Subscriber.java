/**
 * 
 */
package com.phroogal.core.notification;


/**
 * Implementation of the Observer Pattern. Subscriber classes implementing this 
 * interface would be notified of changes by the publisher classes it is registered to 
 * @author Christopher Mariano
 *
 */
public interface Subscriber<T> {

	/**
	 * Routine to be executed on notification by the publisher 
	 * @param subject - observed class
	 */
	public void onNotify(T subject);
}
