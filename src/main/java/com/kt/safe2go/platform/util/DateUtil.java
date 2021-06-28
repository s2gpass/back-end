package com.kt.safe2go.platform.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
 
public class DateUtil {

	private static final SimpleDateFormat FORMAT_MILLISECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	private static final SimpleDateFormat FORMAT_MILLISECOND_DB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat FORMAT_NOLINE = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 입력받은 Date -> String (yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String format(Date date) throws Exception {
		return FORMAT.format(date);
	}

	/**
	 * 입력받은 Date -> String (yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date)  throws Exception  {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 입력받은 Date -> String (yyyy-MM-dd HH:mm:ss SSS)
	 * @param date
	 * @return
	 */
	public static String formatWithMil(Date date) throws Exception  {
		return FORMAT_MILLISECOND.format(date);
	}
	
	/**
	 * 오늘 Date -> String (yyyy-MM-dd HH:mm:ss SSS)
	 * @return
	 */
	public static String formatTodayWithMil()  throws Exception {
		return formatWithMil(new Date());
	}

	/**
	 * 오늘 Date -> String (yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static String formatToday()  throws Exception  {
		return format(new Date());
	}

	/**
	 * 오늘 Date -> String (yyyy-MM-dd)
	 * @return
	 */
	public static String formatDateToday() throws Exception  {
		return formatDate(new Date());
	}

	/**
	 * 입력받은 String -> Date (yyyy-MM-dd HH:mm:ss)
	 * @param datetime
	 * @return
	 */
	public static Date parse(String datetime) throws Exception  {
		try {
			return FORMAT.parse(datetime);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static Date parse_noline(String datetime) throws Exception  {
		try {
			return FORMAT_NOLINE.parse(datetime);
		} catch (ParseException e) {
			return new Date();
		}
	}

	/**
	 * 입력받은 String -> Date (yyyy-MM-dd)
	 * @param datetime
	 * @return
	 */
	public static Date parseDate(String datetime) throws Exception  {
		try {
			return DATE_FORMAT.parse(datetime);
		} catch (ParseException e) {
			return new Date();
		}
	}

	/**
	 * 년을 더한다 (yyyy-MM-dd)
	 * @param datetime
	 * @param year
	 * @return
	 */
	public static String addYearDate(String datetime, int year) throws Exception  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));
		cal.add(Calendar.YEAR, year);
		return formatDate(cal.getTime());
	}

	/**
	 * 월을 더한다. (yyyy-MM-dd)
	 * @param datetime
	 * @param month
	 * @return
	 */
	public static String addMonthDate(String datetime, int month)  throws Exception  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));
		cal.add(Calendar.MONTH, month);
		return formatDate(cal.getTime());
	}

	/**
	 * 일을 더한다. (yyyy-MM-dd)
	 * @param datetime
	 * @param day
	 * @return
	 */
	public static String addDayDate(String datetime, int day)  throws Exception {
		return formatDate(addDay(datetime, day));
	}

	/**
	 * 일을 더한다. (yyyy-MM-dd HH:mm:ss))
	 * @param datetime
	 * @param day
	 * @return
	 */
	public static Date addDay(String datetime, int day)  throws Exception  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * 두 날짜 사이의 모든 날짜들을 반환한다.
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getBetweenDays(String start, String end)  throws Exception  {
		List<String> list = new ArrayList<String>();
		try {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			
			Date d1 = DATE_FORMAT.parse(start);
			Date d2 = DATE_FORMAT.parse(end);
			
			c1.setTime( d1 );
			c2.setTime( d2 );
			
			while( c1.compareTo( c2 ) !=1 ) {
				list.add(formatDate(c1.getTime()));
				
				c1.add(Calendar.DATE, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	/**
	 * 입력받은 날짜의 요일을 가져온다. 일요일(1), 월요일(2), 화요일(3) ~~ 토요일(7)<br>
	 * 골드윙과 맞추기 위해 -1을 해줌: 일요일(0), 월요일(1), 화요일(2) ~~ 토요일(6)<br>
	 * 
	 * @param datetime
	 * @return
	 * @exception
	 */
	public static int getDayOfWeek(String datetime)  throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));

		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 오늘이 포함된 주의 일요일(2013-01-06)
	 * @return
	 */
	public static String getSunOfWeek() throws Exception  {
		Calendar cal = Calendar.getInstance();
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		Calendar cal2 = (Calendar) cal.clone();
		cal2.add(Calendar.DAY_OF_MONTH, w * -1);
		
		int y = cal2.get(Calendar.YEAR);
		int m = cal2.get(Calendar.MONTH) + 1;
		int d = cal2.get(Calendar.DATE);
		
		String mm = String.valueOf(m);
		if(mm.length() == 1) {
			mm = "0" + mm;
		}
		
		String dd = String.valueOf(d);
		if(dd.length() == 1) {
			dd = "0" + dd;
		}
		return (y + "-" + mm + "-" + dd);
	}

	/**
	 * 오늘이 포함된 월의 1일과 말일 구하기 
	 * 
	 * @param datetime
	 * @param year
	 * @return
	 */
	public static String[] getMinMax()  throws Exception {
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH);
		int d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(y, m, d);

		int firstday = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String mm = String.valueOf(m + 1);
		if(mm.length() == 1) {
			mm = "0" + mm;
		}

		String firstdayStr = String.valueOf(firstday);
		if(firstdayStr.length() == 1) {
			firstdayStr = "0" + firstdayStr;
		}
		firstdayStr = String.valueOf(y) + "-" + mm + "-" + firstdayStr;

		String lastdayStr = String.valueOf(lastday);
		if(lastdayStr.length() == 1) {
			lastdayStr = "0" + lastdayStr;
		}
		lastdayStr = String.valueOf(y) + "-" + mm + "-" + lastdayStr;
		
		return new String[]{firstdayStr, lastdayStr};
	}

	/**
	 * 년을 반환한다.
	 * @param datetime
	 * @return
	 */
	public static int getYear(String datetime) throws Exception  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));

		return cal.get(Calendar.YEAR);
	}

	/**
	 * 월을 반환한다.
	 * @param datetime
	 * @return
	 */
	public static int getMonth(String datetime) throws Exception  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));

		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 그달의 주차를 반환한다.
	 * 
	 * @param datetime
	 * @return
	 * @exception
	 */
	public static int _getWeekOfMonth(String datetime)  throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(datetime));

		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 그달의 주차를 구한다: 신한 전용
	 * 
	 * @param datetime
	 * @return
	 * @exception
	 */
	public static int getWeekOfMonth(String datetime)  throws Exception  {
		String start = datetime.substring(0, 8) + "01";
		Date day = parseDate(datetime);
		
		int i = 7;
		int w = 1;
		
		while(true) {
			if(day.getTime() < addDay(start, i).getTime()) {
				return w;
			}
			i = i + 7;
			w++;
		}
	}

	/**
	 * 두 날짜 사이의 일수를 반환한다.
	 * @param from
	 * @param to
	 * @return
	 * @throws ParseException
	 */
	public static int getDayCount(String from, String to) throws ParseException {

		long duration = getTimeCount(from, to, DATE_FORMAT.toPattern());

		return (int) (duration / (1000 * 60 * 60 * 24));
	}

	/**
	 * 두 날짜 사이의 시간을 반환한다.
	 * @param from
	 * @param to
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static long getTimeCount(String from, String to, String format) throws ParseException {

		Date d1 = dateFormatCheck(from, format);
		Date d2 = dateFormatCheck(to, format);

		long duration = d2.getTime() - d1.getTime();

		return duration;
	}

	/**
	 * 두 날짜간의 개월수: yyyy-MM-dd
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public static int getMonthCount(String from, String to) throws Exception {
		return getMonthCount(from, to, DATE_FORMAT.toPattern());
	}	

	/**
	 * 두 날짜간의 개월수
	 * @param from
	 * @param to
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static int getMonthCount(String from, String to, String format) throws Exception {
			Date fromDate = dateFormatCheck(from, format);
			Date toDate = dateFormatCheck(to, format);

			// if two date are same, return 0.
			if (fromDate.compareTo(toDate) == 0)
				return 0;

			SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
			SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);

			int fromYear = Integer.parseInt(yearFormat.format(fromDate));
			int toYear = Integer.parseInt(yearFormat.format(toDate));
			int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
			int toMonth = Integer.parseInt(monthFormat.format(toDate));

			int result = 0;
			result += ((toYear - fromYear) * 12);
			result += (toMonth - fromMonth);

			return result;
	}	

	/**
	 * 두 날짜간의 년수: yyyy-MM-dd
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public static int getYearCount(String from, String to) throws Exception {
		return getYearCount(from, to, DATE_FORMAT.toPattern());
	}

	/**
	 * 두 날짜간의 년수
	 * @param from
	 * @param to
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static int getYearCount(String from, String to, String format) throws Exception {
			Date fromDate = dateFormatCheck(from, format);
			Date toDate = dateFormatCheck(to, format);

			// if two date are same, return 0.
			if (fromDate.compareTo(toDate) == 0)
				return 0;

			SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);

			int fromYear = Integer.parseInt(yearFormat.format(fromDate));
			int toYear = Integer.parseInt(yearFormat.format(toDate));

			int result = 0;
			result += (toYear - fromYear);

			return result;
	}	
	
	/**
	 * 입력받은 날짜의 포멧의 유효성을 검사한다.
	 * @param source
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date dateFormatCheck(String source, String format) throws ParseException {

		if (source == null) {
			throw new ParseException("date string to check is null", 0);
		}

		if (format == null) {
			throw new ParseException("format string to check date is null", 0);
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);

		Date date = null;

		try {
			date = formatter.parse(source);
		} catch (ParseException e) {
			throw new ParseException(" wrong date:\"" + source + "\" with format \"" + format + "\"", 0);
		}

		if (!formatter.format(date).equals(source)) {
			throw new ParseException("Out of bound date:\"" + source + "\" with format \"" + format + "\"", 0);
		}

		return date;
	}
	
	public static String getTimeStamp(){
	    //Date object
	    Date date= new Date();
	    //getTime() returns current time in milliseconds
	    long time = date.getTime();
	    //Passed the milliseconds to constructor of Timestamp class 
	    Timestamp ts = new Timestamp(time);
	    
	    return Long.valueOf(ts.getTime()).toString();
	}
	
	/**
	 * DB 처리용 현재시간 -> String (yyyy-MM-dd HH:mm:ss.SSSSSS)
	 * @return
	 */
	public static String getFormatWithMilDB() {
		return FORMAT_MILLISECOND_DB.format(new Date());
	}

	/**
	 *
	 * @param yyyyMMddHHmmss 해당 형식의 날짜를 시스템 수집 시간으로 바꾸어서 저장
	 * @return
	 */
	public static long transferTimeToUTCTime(String yyyyMMddHHmmss) {
		try {
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime ldt = LocalDateTime.parse(yyyyMMddHHmmss, formatter);
			return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}catch (Exception e) {
			return System.currentTimeMillis();
		}
	}
}

