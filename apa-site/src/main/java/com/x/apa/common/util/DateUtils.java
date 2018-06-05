package com.x.apa.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * @author liumeng
 */
public class DateUtils {

	/**
	 * 获取前一天日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date beforeDayDate(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -amount);

		return c.getTime();
	}

	public static Date afterDayDate(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, amount);

		return c.getTime();
	}

	public static int getDayOfWeek(Date d) {
		Calendar c = Calendar.getInstance();
		String firstDayOfWeek = "2";/*
									 * PropertiesAccessorUtil.getProperty(
									 * "firstDayOfWeek") == null ? "2" :
									 * PropertiesAccessorUtil.getProperty(
									 * "firstDayOfWeek");
									 */
		c.setTime(d);

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (StringUtils.equals(firstDayOfWeek, "2")) {
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (dayOfWeek == 0) {
				dayOfWeek = 7;
			}
		}
		return dayOfWeek;
	}

	public static int getDayOfMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static String toString_YYYY_MM_DD(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String toString_YYYY_MM_DD_HH_MM_SS(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static Date parseYYYY_MM_DDDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException("fail to parse date from " + strDate);
		}
	}

	public static Date parseYYYY_MM_DD_HH_MM_SSDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException("fail to parse date from " + strDate);
		}
	}
}
