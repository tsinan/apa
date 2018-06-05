package com.x.apa.common.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * @author liumeng
 */
public class CsvUtils {

	/**
	 * 将CSV（Comma-seprate Value）转换为List
	 * 
	 * @param valueCSV
	 * @return
	 */
	public static List<String> convertCSVToList(String valueCSV) {

		List<String> valueList = Lists.newArrayList();
		if (StringUtils.isBlank(valueCSV)) {
			return valueList;
		}

		String[] valueStrAry = valueCSV.split(",");
		for (String valueStr : valueStrAry) {
			if (StringUtils.isBlank(valueCSV)) {
				continue;
			}

			valueList.add(valueStr);
		}

		return valueList;
	}
}
