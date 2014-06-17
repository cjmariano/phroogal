package com.phroogal.core.repository;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Dictionary;

public class DictionaryRepositoryTest extends BaseRepositoryTest<Dictionary, ObjectId, DictionaryRepository> {

	@Autowired
	private DictionaryRepository repository;
	
	@Override
	protected DictionaryRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Dictionary> returnDomainClass() {
		return Dictionary.class;
	}

	@Override
	protected Dictionary createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestDictionary();
	}

	@Override
	protected void modifyDomain(Dictionary domain) {
		domain.setTerm(domain.getTerm() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(Dictionary domain, Dictionary other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getTerm(), other.getTerm());
			Assert.assertEquals(domain.getDefinition(), other.getDefinition());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
}
