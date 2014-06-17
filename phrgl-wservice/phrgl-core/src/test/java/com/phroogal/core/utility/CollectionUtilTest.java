package com.phroogal.core.utility;

import static com.phroogal.core.utility.CollectionUtil.arrayList;
import static com.phroogal.core.utility.CollectionUtil.getFirstItem;
import static com.phroogal.core.utility.CollectionUtil.hashMap;
import static com.phroogal.core.utility.CollectionUtil.hashSet;
import static com.phroogal.core.utility.CollectionUtil.hashTable;
import static com.phroogal.core.utility.CollectionUtil.linkedHashMap;
import static com.phroogal.core.utility.CollectionUtil.removeDuplicates;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.phroogal.core.domain.SortableByRank;

/**
 * @author stay2
 * 
 */
public class CollectionUtilTest {

    @Test
    public void testArrayList() {
        List<Integer> integers = arrayList();
        assertNotNull(integers);
    }

    @Test
    public void testArrayListWithInputCollection() {
        List<Integer> base = arrayList();
        base.add(1);
        List<Integer> integers = arrayList(base);
        assertNotNull(integers);
        assertTrue(integers.size() == 1);
        assertEquals(Integer.valueOf(1), integers.get(0));

        integers = arrayList(1, 2);
        assertNotNull(integers);
        assertTrue(integers.size() == 2);
        assertEquals(Integer.valueOf(1), integers.get(0));
        assertEquals(Integer.valueOf(2), integers.get(1));
    }

    @Test
    public void testHashSet() {
        Set<Integer> integers = hashSet();
        assertNotNull(integers);
    }

    @Test
    public void testHashSetWithInputCollection() {
        List<Integer> base = arrayList();
        base.add(1);
        Set<Integer> integers = hashSet(base);
        assertNotNull(integers);
        assertTrue(integers.size() == 1);
        assertEquals(Integer.valueOf(1), integers.iterator().next());

        integers = hashSet(1);
        assertNotNull(integers);
        assertTrue(integers.size() == 1);
        assertEquals(Integer.valueOf(1), integers.iterator().next());
    }

    @Test
    public void testHashMap() {
        Map<String, Integer> integers = hashMap();
        assertNotNull(integers);
    }
    
    @Test
    public void testLinkedHashMap() {
        Map<String, Integer> integers = linkedHashMap();
        assertNotNull(integers);
        integers.put("3", 3);
        integers.put("2", 2);
        integers.put("1", 1);
        
        int test = 3;
        for (Map.Entry<String, Integer> entrySet : integers.entrySet()) {
            assertEquals(String.valueOf(test), entrySet.getKey());
            assertEquals(test, entrySet.getValue().intValue());
            test--;
        }
    }

    @Test
    public void testHashTable() {
        Map<String, Integer> integers = hashTable();
        assertNotNull(integers);
    }

    @Test
    public void testGetFirstItem() {
        List<Integer> list = null;
        assertNull(getFirstItem(list));
        list = arrayList();
        assertNull(getFirstItem(list));
        list.add(1);
        assertEquals(Integer.valueOf(1), getFirstItem(list));

    }

    @Test
    public void testRemoveDuplicates() {
    	List<String> list = arrayList();
    	list.add("A");
    	list.add("B");
    	list.add("C");
    	list.add("A");
    	list.add("A");
    	list.add("A");
    	
    	list = removeDuplicates(list);
    	assertTrue(list.size() == 3);
    }
    
    @Test
	    public void testSortElementsByDescRank() {
	    	List<SortableByRank> elements = CollectionUtil.arrayList();
	    	SortableByRank elem1 = generateTestSortableByRankElement();
			elem1.setRank(10);
			elements.add(elem1);
			SortableByRank elem2 = generateTestSortableByRankElement();
			elem2.setRank(50);
			elements.add(elem2);
			SortableByRank elem3 = generateTestSortableByRankElement();
			elem3.setRank(100);
			elements.add(elem3);
			CollectionUtil.sortElementsByDescRank(elements);
			Assert.assertTrue(elements.get(0).equals(elem3));
			Assert.assertTrue(elements.get(1).equals(elem2));
			Assert.assertTrue(elements.get(2).equals(elem1));
	    }

	private SortableByRank generateTestSortableByRankElement() {
		SortableByRank element = new SortableByRank() {
    		
    		private double rank;
    		
			@Override
			public void setRank(double rank) {
				this.rank = rank;
				
			}
			
			@Override
			public double getRank() {
				return rank;
			}
		};
		return element;
	}
}
