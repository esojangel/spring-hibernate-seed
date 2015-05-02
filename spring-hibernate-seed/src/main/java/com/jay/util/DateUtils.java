package com.jay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class DateUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static ThreadLocal<SimpleDateFormat> formatDate = new ThreadLocal<SimpleDateFormat>();

	private static ThreadLocal<SimpleDateFormat> formatTime = new ThreadLocal<SimpleDateFormat>();

	private static ThreadLocal<SimpleDateFormat> formatTmpl = new ThreadLocal<SimpleDateFormat>();

	public static SimpleDateFormat getDateFormat() {
		SimpleDateFormat fmt = formatDate.get();
		if (fmt == null) {
			fmt = new SimpleDateFormat(DATE_FORMAT);
			formatDate.set(fmt);
		}
		return fmt;
	}

	public static SimpleDateFormat getTimeFormat() {
		SimpleDateFormat fmt = formatTime.get();
		if (fmt == null) {
			fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
			formatTime.set(fmt);
		}
		return fmt;
	}

	private static SimpleDateFormat getTmplFormat() {
		SimpleDateFormat fmt = formatTmpl.get();
		if (fmt == null) {
			fmt = new SimpleDateFormat(DATE_FORMAT);
			formatTmpl.set(fmt);
		}
		return fmt;
	}

	public static String format(Date date) {
		if (date == null) {
			return "";
		}
		return getDateFormat().format(date);
	}

	public static String format(Date date, String pattern) {
		SimpleDateFormat fmt = getTmplFormat();
		fmt.applyPattern(pattern);
		return fmt.format(date);
	}

	public static Date parse(String source) throws ParseException {
		return getDateFormat().parse(source);
	}

	public static Date parse(String source, String pattern)
			throws ParseException {
		SimpleDateFormat fmt = getTmplFormat();
		fmt.applyPattern(pattern);
		return fmt.parse(source);
	}

	public static String toDateString(Date date) {
		return getDateFormat().format(date);
	}

	public static String toDateTimeString(Date date) {
		return getTimeFormat().format(date);
	}

	public static int isEqual(Date DATE1, Date DATE2) {
		try {
			SimpleDateFormat fmt = getDateFormat();
			String str1 = fmt.format(DATE1);
			String str2 = fmt.format(DATE2);

			Date dt1 = fmt.parse(str1);
			Date dt2 = fmt.parse(str2);

			double diff = (dt1.getTime() - dt2.getTime()) * 1d;
			return (int) Math.signum(diff);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static String getDiff(Date startDate, Date endDate) {
		long diff = (endDate.getTime() - startDate.getTime());// 得到两者的毫秒数
		diff = Math.abs(diff);
		long day = diff / (24 * 60 * 60 * 1000);
		long hour = (diff / (60 * 60 * 1000) - day * 24);
		long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
				- min * 60 * 1000 - s * 1000);
		if (day > 0) {
			return day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
		} else if (hour > 0) {
			return hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
		} else if (min > 0) {
			return min + "分" + s + "秒" + ms + "毫秒";
		}
		return s + "秒" + ms + "毫秒";
	}
}
