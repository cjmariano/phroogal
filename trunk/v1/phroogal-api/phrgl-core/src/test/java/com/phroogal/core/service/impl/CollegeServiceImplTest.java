package com.phroogal.core.service.impl;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.College;
import com.phroogal.core.repository.CollegeRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.CollegeService;
import com.phroogal.core.service.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class CollegeServiceImplTest extends BaseServiceTest<College, ObjectId, CollegeRepository> {

	@Autowired
	private CollegeService serviceImpl;
	
	@Override
	protected Service<College, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected CollegeRepository returnMongoRepository() {
		return Mockito.mock(CollegeRepository.class);
	}

	@Override
	protected College returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestCollege();
	}

	@Test
	public void testFindByNameContaining() throws Exception {
		CollegeRepository repository = Mockito.mock(CollegeRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "collegeRepository", repository);
		serviceImpl.findByNameContaining("keyword");
		verify(repository, atLeastOnce()).findByNameLike("keyword");
	}

}
