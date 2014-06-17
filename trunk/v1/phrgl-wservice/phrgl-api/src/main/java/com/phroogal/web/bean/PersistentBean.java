package com.phroogal.web.bean;

/**
 * Base bean for classes that should have IDs
 * @author Christopher Mariano
 *
 */
public class PersistentBean<ID> {

	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}