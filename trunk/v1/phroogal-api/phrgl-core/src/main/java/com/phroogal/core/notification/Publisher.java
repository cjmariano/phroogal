package com.phroogal.core.notification;

import java.io.Serializable;
import java.util.List;

import com.phroogal.core.domain.Persistent;



/**
 * Implementation of the Observer Pattern. Publisher classes implementing this
 * interface, is responsible for notifying subscribers of changes in the observed
 * subject/domain 
 * @author Christopher Mariano
 *
 */
public interface Publisher {

	/**
	 * Notify subscribers of a change in the observed subject 
	 * @param subject - object being observed
	 */
	public void notify(Persistent<? extends Serializable> subject);
	
	/**
	 * Retrieves a list of {@link Subscriber} to be notified
	 * @return list of {@link Subscriber}
	 */
	@SuppressWarnings("rawtypes")
	public List<Subscriber> getSubscribers();
}
