/**
 * 
 */
package com.phroogal.core.utility;

import org.springframework.scheduling.annotation.Async;

/**
 * Enables tasks to be executed asynchronously
 * @author Christopher Mariano
 *
 */
public abstract class AsynchronousTask {

	/**
	 * Executes the tasks to be done asynchronously
	 */
	protected abstract void doTask();
	
	/**
	 * Executes the task declared asynchronously
	 */
	@Async
	public void start() {
		doTask();
	}
}
