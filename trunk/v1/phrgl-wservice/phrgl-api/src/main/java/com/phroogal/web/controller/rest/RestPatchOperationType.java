package com.phroogal.web.controller.rest;

import com.phroogal.core.domain.PropertyModifiable;
import com.phroogal.core.valueobjects.PropertyBag;

/**
 * Enumerates operation types for REST Patch method<br> 
 * 
 * @author Christopher Mariano
 *
 */
public enum RestPatchOperationType {
	
	/**
	 * (equivalent to POST): Add the given attribute value to a collection.
	 */
	INCLUDE {
		
		@Override
		public boolean isSupported() {
			return false;
		}

		@Override
		public void execute(PropertyModifiable modifiable, PropertyBag propertyBag) {
			throw new UnsupportedOperationException();
		}
	}, 
	/**
	 * (equivalent to one form of PUT): Add the given attribute key-value pair at the location implied by the key. (If there’s already an attribute with this key, return an error status.)<br>
	 */
	PLACE {
		
		@Override
		public boolean isSupported() {
			return false;
		}
		
		@Override
		public void execute(PropertyModifiable modifiable, PropertyBag propertyBag) {
			throw new UnsupportedOperationException();
		}
	}, 
	
	/**
	 * (equivalent to another form of PUT): Replace the current value of the given key with this new value. (If there’s no such key, return an error status.)<br>
	 */
	REPLACE {
		
		@Override
		public boolean isSupported() {
			return true;
		}
		
		@Override
		public void execute(PropertyModifiable modifiable, PropertyBag propertyBag) throws Exception{
			modifiable.partialUpdate(propertyBag);
		}
	}, 
	
	/**
	 * (equivalent to a third form of PUT): This means PLACE or REPLACE. (At the end of this operation, we want the specified key to refer to the given value whether the key already existed or not.)<br>
	 */
	FORCE {
		
		@Override
		public boolean isSupported() {
			return false;
		}
		
		@Override
		public void execute(PropertyModifiable modifiable, PropertyBag propertyBag) {
			throw new UnsupportedOperationException();
		}
	}, 
	
	/**
	 * (equivalent to DELETE): Delete, deactivate or otherwise render inaccessible the attribute with the given key.<br>
	 */
	RETIRE {
		
		@Override
		public boolean isSupported() {
			return false;
		}
		
		@Override
		public void execute(PropertyModifiable modifiable, PropertyBag propertyBag) {
			throw new UnsupportedOperationException();
		}
	};
	
	/**
	 * Indicates if this operation is supported or not
	 * @return true if supported, false otherwise
	 */
	public abstract boolean isSupported();
	
	/**
	 * Executes the operation
	 * @param modifiable object to execute the operation on.
	 * @param propertyBag contains the key-value pair for editing the given modifiable entity
	 */
	public abstract void execute(PropertyModifiable modifiable, PropertyBag propertyBag) throws Exception;
}
