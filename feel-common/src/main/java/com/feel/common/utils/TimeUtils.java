package com.feel.common.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类：日期和时间的相关操作
 */
public class TimeUtils implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;
	private static int MaxDate;// 一月最大天数
	private static int MaxYear;// 一年最大天数

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static int dayInMills = 0x5265c00;

	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 以输入日期为基准日期，进行日期的前推或后推
	 *
	 * @param date 基准日期
	 * @param len 正数为向后推，负数为向前推
	 */
	public static String dayMove(String date, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DATE, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	public static String dayMove(String date, int len, String formart) {
		SimpleDateFormat sdf = new SimpleDateFormat(formart);
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DATE, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 获取输入日期离当前日期的天数
	 *
	 * @param date
	 *            - 目标日期
	 * @return 输入日期小与当前日期，则结果为负；否则为正。
	 */
	public static int daysBeforeToday(String date) {
		long diff = 0L;
		try {
			Date now = df.parse(getToday());
			Date pre = df.parse(date);
			diff = (pre.getTime() - now.getTime()) / dayInMills;
		} catch (Exception _ex) {
		}
		return (int) diff;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @param formatStr 日期格式(默认yyyy-MM-dd HH:mm:ss)
	 * @return 日期格式字符串
	 */
	public static String timeStamp2Date(String seconds,String formatStr) {
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(formatStr == null || formatStr.isEmpty()) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(new Date(Long.valueOf(seconds+"000")));
	}
	/**
	 * 日期格式字符串转换成时间戳（毫秒）
	 * @param dateStr 字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return 时间戳（毫秒）
	 */
	public static long date2TimeStamp(String dateStr,String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateStr).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取1970年1月1日至当前时间的毫秒数
	 */
	public static long getTimeStamp() {
		return (new Date()).getTime();
	}

	/**
	 * 获取8位格式的当前日期
	 */
	public static String getToday() {
		Date date = new Date();
		String now = df.format(date);
		return now;
	}

	/**
	 * 获取8位格式的昨天的日期
	 */
	public static String getYesterday() {
		Date today = new Date();
		Date date = new Date(today.getTime() - dayInMills);
		String yesterday = df.format(date);
		return yesterday;
	}

	/**
	 * 获取8位格式的日期(yyyyMMdd)
	 *
	 * @param date
	 *            Date日期对象
	 */
	public static String getStringOfDate(Date date) {
		return df.format(date);
	}

	/**
	 * 获取指定格式的日期
	 *
	 * @param date
	 *            Date日期对象
	 */
	public static String getStringOfDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 由8位字符串形式的日期转换成Date格式日期对象
	 *
	 * @param date
	 *            - 8位字符串形式的日期
	 */
	public static Date getDateOfString(String date) {
		try {
			return df.parse(date);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static Date getDateOfString(String date, SimpleDateFormat sdf) {
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static Date getDateOfString(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 变换日期
	 *
	 * @param _src 原日期
	 * @param t 移动数据类型1-日，2-月，3-周
	 * @param _offset 偏移量
	 */
	public final static String getDateMove(String _src, int t, int _offset) {
		int length = _src.length();
		String src = _src;
		if (length == 10) {
			src = src.replaceAll("-", "");
		}
		int offset = _offset;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		String result = null;
		int type = 0;

		try {
			switch (t) {
			case 1: // 日
				type = Calendar.DATE;
				break;
			case 2: // 月
				type = Calendar.MONTH;
				break;
			case 3: // 周
				type = Calendar.WEEK_OF_YEAR;
				break;
			case 4: // 年
				type = Calendar.YEAR;
				break;
			default: // 默认，日
				type = Calendar.DAY_OF_YEAR;
				break;
			}

			int year = Integer.valueOf(src.substring(0, 4)).intValue();
			int month = Integer.valueOf(src.substring(4, 6)).intValue();
			int day = Integer.valueOf(src.substring(6)).intValue();
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, day, 0, 0, 0);
			calendar.add(type, offset);
			result = f.format(calendar.getTime());
			if (length == 10) {
				result = df.format(calendar.getTime());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取在输入的基准年份上前推或后推range个type后得到的日期段
	 *
	 * @param year
	 *            - 基准年yyyy
	 * @param type
	 *            - 0-周，1-月,2-季,3-半年,4-年
	 * @param range
	 *            - 范围值
	 * @return 包含"beginDate"和"endDate"的Map集合
	 */
	public static Map<String, String> getDateRange(int year, int type, int range) {
		Map<String, String> result = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 0, 1, 0, 0, 0);
		String beginDate = null;
		String endDate = null;

		switch (type) {
		case 0: // 周
			calendar.add(Calendar.WEEK_OF_YEAR, range);
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getMinimum(Calendar.DAY_OF_WEEK));
			beginDate = f.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
			endDate = f.format(calendar.getTime());
			break;
		case 1: // 月
			calendar.add(Calendar.MONTH, range);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
			beginDate = f.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = f.format(calendar.getTime());
			break;
		case 2: // 季
			calendar.add(Calendar.MONTH, range * 3);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
			beginDate = f.format(calendar.getTime());
			calendar.add(Calendar.MONTH, 2);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = f.format(calendar.getTime());
			break;
		case 3: // 半年
			calendar.add(Calendar.MONTH, range * 6);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
			beginDate = f.format(calendar.getTime());
			calendar.add(Calendar.MONTH, 5);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = f.format(calendar.getTime());
			break;
		case 4: // 年
			calendar.add(Calendar.YEAR, range);
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getMinimum(Calendar.DAY_OF_YEAR));
			beginDate = f.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
			endDate = f.format(calendar.getTime());
			break;
		default:
			return null;
		}

		result = new HashMap<String, String>();
		result.put("beginDate", beginDate);
		result.put("endDate", endDate);
		return result;
	}

	/**
	 * 时间前推或后推分钟,其中second表示秒.
	 *
	 * @param timestr
	 * @param second
	 * @param format
	 * @return @
	 */
	public static String getPreTime(String timestr, int second, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(timestr);
			long Time = (date.getTime() / 1000) + second;
			date.setTime(Time * 1000);
			return new SimpleDateFormat(format).format(date);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到指定月后的日期
	 */

	public static String getAfterMonth(int month, String specifiedDay) {
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(specifiedDay);// 初始日期
		} catch (Exception e) {

		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, month);// 在日历的月份上增加6个月
		String strDate = sdf.format(c.getTime());// 的到你想要得6个月后的日期
		return strDate;

	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date date = myFormatter.parse(sj1);
			Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date date = formatter.parse(sdate, pos);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour 中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 *
	 */
	public static Date strToDate(String strDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	// 将时分秒转化成分钟数
	public static long dateToSeconds(String strDate) {
		String[] s = strDate.split(":");
		long hours = Long.parseLong(s[0]) * 3600;
		long minutes = Long.parseLong(s[1]) * 60;
		double seconds = 0;
		if (s.length > 2) {
			seconds = Double.parseDouble(s[2]);
		}
		return (long) (hours + minutes + seconds);
	}

	/**
	 * 两个时间之间的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	// 计算当月最后一天,返回字符串
	public static String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1 号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 上月第一天
	public static String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1 号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当月第一天
	public static String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当天时间
	public static String formatDate(Date date, String dateformat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		return dateFormat.format(date);
	}

	// 获取当天时间
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获取当天时间
	public static String getNowTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获取当天时间
	public static String getNowDate() {
		return getNowTime("yyyy-MM-dd");
	}

	// 获取当天时间
	public static Date getDateNow() {
		return new Date();
	}


	// 获得当前日期与本周日相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得本周星期日的日期
	public static String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得本周一的日期
	public static String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得本周一的日期
	public static String getMondayOFWeek2() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	// 获得相应周的周六的日期
	public static String getSaturday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期一的日期
	public static String getPreviousWeekday() {
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期一的日期
	public static String getNextMonday() {
		weeks++;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期日的日期
	public static String getNextSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	public static int getMonthPlus() {
		Calendar cd = Calendar.getInstance();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	// 获得上月最后一天的日期
	public static String getPreviousMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月第一天的日期
	public static String getNextMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月第一天的日期
	public static String getNextMonthFirst(String specifiedDay) {
		try {
			String str = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar lastDate = Calendar.getInstance();
			Date date = sdf.parse(specifiedDay);
			lastDate.setTime(date);
			lastDate.add(Calendar.MONTH, 1);// 减一个月
			lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
			str = sdf.format(lastDate.getTime());
			return str;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	// 获得下个月最后一天的日期
	public static String getNextMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();

		lastDate.add(Calendar.MONTH, 1);// 加一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年最后一天的日期
	public static String getNextYearEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年第一天的日期
	public static String getNextYearFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得本年有多少天
	public static int getMaxYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	private static int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	// 获得本年第一天的日期
	public static String getCurrentYearFirst() {
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	// 获得本年最后一天的日期 *
	public static String getCurrentYearEnd() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		return years + "-12-31";
	}

	// 获得上年第一天的日期 *
	public static String getPreviousYearFirst() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		years_value--;
		return years_value + "-1-1";
	}

	// 获得本季度
	public static String getThisSeasonTime(int month) {
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
		return seasonDate;
	}

	/**
	 * 获取某年某月的最后一天
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 *
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	public static String getWeekOfDate(String dta) throws ParseException {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = myFormatter.parse(dta);

		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static String formatDate(String date, String format) {
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat(format);
			Date dt = myFormatter.parse(date);

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			String dateString = formatter.format(dt);
			return dateString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取当天时间加上若干天的时间
	public static String getAfterTime(String dateformat, int count) {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, count); // 得到前一天
		String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return afterDay;
	}

	/**
	 * 比较两个时间大小
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		return compareDate(date1, date2, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @功能描述：比较时间/日期的大小
	 * @param endDate
	 *            结束
	 * @param startDate
	 *            开始
	 * @param format
	 *            时间/日期格式，默认yyyy-MM-dd HH:mm:ss格式
	 * @return
	 */
	public static int compareDate(String endDate, String startDate, String format) {
		DateFormat df = new SimpleDateFormat(StringUtils.isNull(format) ? "yyyy-MM-dd HH:mm:ss" : format);
		try {
			Date eDate = df.parse(endDate);
			Date sDate = df.parse(startDate);
			if (eDate.getTime() > sDate.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (eDate.getTime() < sDate.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param specifiedDay
	 * @return @
	 */
	public static String getSpecifiedDayBefore(String specifiedDay, String formatStr) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(formatStr).parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	// 获取当天时间加上若干天的时间
	public static String getSpecifiedDayTime(String startDate, String formatStr, int count) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd"); // 字符串转换
		Calendar c = Calendar.getInstance();
		String tempDateStr = TimeUtils.getSpecifiedDayBefore(startDate, formatStr);
		Date tempDate = TimeUtils.strToDate(tempDateStr);
		c.setTimeInMillis(tempDate.getTime());
		c.add(Calendar.DATE, count);// 天后的日期
		Date date = new Date(c.getTimeInMillis()); // 将c转换成Date
		return formatDate.format(date);
	}

	/**
	 * 时区 时间转换方法:将传入的时间（可能为其他时区）转化成目标时区对应的时间
	 * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
	 * @param sourceId 入参的时间的时区id 比如：+08:00
	 * @param targetId 要转换成目标时区id 比如：+09:00
	 * @param reFormat 返回格式 默认：yyyy-MM-dd HH:mm:ss
	 * @return string 转化时区后的时间
	 */
	public static String timeConvert(String sourceTime, String sourceId,
									 String targetId, String reFormat) {
		//校验入参是否合法
		if (null == sourceId || "".equals(sourceId) || null == targetId
				|| "".equals(targetId) || null == sourceTime
				|| "".equals(sourceTime)) {
			return null;
		}

		if (StringUtils.isNull(reFormat)) {
			reFormat = "yyyy-MM-dd HH:mm:ss";
		}

		//校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
		String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
		if (!sourceTime.matches(reg)) {
			return null;
		}

		try {
			//时间格式
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//根据入参原时区id，获取对应的timezone对象
			TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT" + sourceId);
			//设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
			df.setTimeZone(sourceTimeZone);
			//将字符串sourceTime转化成原时区对应的date对象
			Date sourceDate = df.parse(sourceTime);

			//开始转化时区：根据目标时区id设置目标TimeZone
			TimeZone targetTimeZone = TimeZone.getTimeZone("GMT" + targetId);
			//设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
			df.setTimeZone(targetTimeZone);
			//得到目标时间字符串
			String targetTime = df.format(sourceDate);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(targetTime);
			sdf = new SimpleDateFormat(reFormat);

			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获得上周星期日的日期
	public String getPreviousWeekSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上年最后一天的日期
	public String getPreviousYearEnd() {
		weeks--;
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		getThisSeasonTime(11);
		return preYearDay;
	}
}
