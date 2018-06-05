package com.x.apa.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.x.apa.common.pageable.Sort;

/**
 * @author liumeng
 */
public class MapUtils {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Sort.Direction direction) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				if (direction == Sort.Direction.ASC) {
					return (o1.getValue()).compareTo(o2.getValue());
				} else {
					return -(o1.getValue()).compareTo(o2.getValue());
				}
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
