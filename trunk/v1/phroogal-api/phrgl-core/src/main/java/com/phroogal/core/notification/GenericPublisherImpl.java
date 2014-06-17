/**
 * 
 */
package com.phroogal.core.notification;

import java.io.Serializable;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.phroogal.core.domain.Persistent;

/**
 * Implementation of {@link Publisher}
 * @author Christopher Mariano
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class GenericPublisherImpl implements Publisher {

	private List<Subscriber> subscribers;

	@Async
	@Override
	public void notify(Persistent<? extends Serializable> subject) {
		for (Subscriber subscriber : subscribers) {
			subscriber.onNotify(subject);
		}
	}

	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
}
