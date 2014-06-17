package com.phroogal.core.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.phroogal.core.domain.SortableByRank;

/**
 * Houses factory methods that returns generic collections without the verbosity of the declaring the parameterised
 * types twice in the constructors, i.e. turn this:
 * 
 * List<Type> list = new ArrayList<Type>();
 * 
 * to:
 * 
 * List<Type> list = CollectionUtil.arrayList();
 * 
 * @author Christopher Mariano
 * 
 */
public final class CollectionUtil {

    public static <E> List<E> arrayList() {
        return new ArrayList<E>();
    }

    public static <E> List<E> arrayList(Collection<E> collection) {
        return new ArrayList<E>(collection);
    }

    public static <E> List<E> arrayList(E... array) {
        List<E> list = arrayList();
        list.addAll(Arrays.asList(array));
        return list;
    }

    public static <T> T getFirstItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public static <T> T getLastItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    public static <K, V> Map<K, V> hashMap() {
        return new HashMap<K, V>();
    }

    public static <E> Set<E> hashSet() {
        return new HashSet<E>();
    }

    public static <E> Set<E> hashSet(Collection<E> collection) {
        return new HashSet<E>(collection);
    }

    public static <E> Set<E> hashSet(E... array) {
        Set<E> set = hashSet();
        set.addAll(Arrays.asList(array));
        return set;
    }

    public static <K, V> Map<K, V> hashTable() {
        return new Hashtable<K, V>();
    }

    public static <T> List<T> joinList(List<T>... lists) {
        ArrayList<T> result = new ArrayList<T>();
        for (List<T> l : lists) {
            result.addAll(l);
        }
        return result;
    }

    public static <K, V> Map<K, V> linkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <E> Set<E> linkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <E> Set<E> linkedHashSet(Collection<E> collection) {
        return new LinkedHashSet<E>(collection);
    }

    public static <E> Set<E> linkedHashSet(E... array) {
        Set<E> set = linkedHashSet();
        set.addAll(Arrays.asList(array));
        return set;
    }
    
    public static <E> List<E> removeDuplicates(List<E> collection) {
    	if ( ! CollectionUtils.isEmpty(collection)) {
    		Set<E> set = new HashSet<E>(collection);
            return new ArrayList<E>(set);	
    	}
    	return new ArrayList<E>();
    }
    
    /**
     * Sorts a collection containing elements which are subtypes of {@link SortableByRank} by 
     * descending rank
     * @param collection to be sorted
     * @return the collection
     */
    public static <E extends SortableByRank> List<E> sortElementsByDescRank(List<E> collection) {
    	Collections.sort(collection, new Comparator<E>(){
			
			/**
			 * Method implementation to sort by descending order based on rank
			 * @param answerA first answer argument
			 * @param answerB second answer argument
			 * @return ordinal
			 */
    		@Override
			public int compare(E elementA, E elementB) {
				if (elementA instanceof SortableByRank && elementB instanceof SortableByRank) {
					return new Double( ((SortableByRank) elementB).getRank())
					.compareTo( ((SortableByRank) elementA).getRank());	
				}
				return 0;
			}
			
        });
		return collection;
    }

    private CollectionUtil() {
        // ensures no instantiability
    }

}
