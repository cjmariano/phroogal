/**
 * 
 */
package com.phroogal.core.utility;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Christopher Mariano
 *
 */
public class PaginatedListTest {
	
	private final String ELEM_NAME_FORMAT = "PageAt[%d] | PageSize[%d]";

	@Test
	public void testGetListForPage() throws Exception {
		List<String> list = createTestList();
		
		int pageAt = 0;
		int pageSize = 10;
		List<String> paginatedList = new PaginatedList<String>(list, pageAt, pageSize).getListForPage();
		assertListHasCorrectElements(paginatedList, pageAt);
		Assert.assertTrue(paginatedList.size() == pageSize);
		
		pageAt = 1;
		paginatedList = new PaginatedList<String>(list, pageAt, pageSize).getListForPage();
		assertListHasCorrectElements(paginatedList, pageAt);
		Assert.assertTrue(paginatedList.size() == pageSize);
		
		pageAt = 2;
		paginatedList = new PaginatedList<String>(list, pageAt, pageSize).getListForPage();
		assertListHasCorrectElements(paginatedList, pageAt);
		Assert.assertTrue(paginatedList.size() == pageSize);
	}

	@Test
	public void testGetList() throws Exception {
		List<String> list = createTestList();
		List<String> paginatedList = new PaginatedList<String>(list, 0, 10).getList();
		Assert.assertTrue(paginatedList.size()== 30);
	}
	
	private List<String> createTestList() {
		List<String> list  = CollectionUtil.arrayList();
		generateElementsForPage(list, 0);
		generateElementsForPage(list, 1);
		generateElementsForPage(list, 2);
		return list;
	}
	
	private void assertListHasCorrectElements(List<String> paginatedList, int pageAt) {
		for (int i = 1; i <= 10; i++) {
			String elem = paginatedList.get(i - 1);
			Assert.assertEquals( String.format(ELEM_NAME_FORMAT, pageAt, i) ,elem);
		}
	}

	private void generateElementsForPage(List<String> list, int pageAt) {
		for (int i = 1; i <= 10; i++) {
			list.add(String.format(ELEM_NAME_FORMAT, pageAt, i));
		}
	}

}
