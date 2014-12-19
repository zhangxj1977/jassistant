package org.jas.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class DateUtil {
	public static final long DAY_MILLI = 24*60*60*1000;
	public final static String DATE_FORMAT_DATEONLY = "yyyy/MM/dd";
	public final static String DATE_FORMAT_TIMEONLY = "HH:mm:ss";
	public final static String DATE_FORMAT_DATETIME   = "yyyy/MM/dd HH:mm:ss";
	private static SimpleDateFormat sdfDateOnly = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATEONLY );
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATETIME );
	private GregorianCalendar  gcal  = null;
	private Timestamp time = null;
	public final static SimpleDateFormat sFormat= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/************************************************
	 *  private attribute
	 ************************************************/
	public DateUtil() {
		gcal = new GregorianCalendar();
		time = new Timestamp(System.currentTimeMillis() );
	}

	/*****************************************************
	 * String ==> java.util.Date
	 *****************************************************/
	/**
	 * YYYY/MM/DD
	 * @param  sDate Date string
	 * @return Date
	 */
	public static java.util.Date toDate( String sDate) {
		return toDate( sDate, DateUtil.sdfDateOnly  );
	}

	/**
	 * Format String to Date
	 *
	 * @param  sDate Date string
	 * @param  sFmt  Date format ,  DATE_FORMAT_DATEONLY or  DATE_FORMAT_DATETIME
	 *
	 * @return Date
	 */
	public static java.util.Date toDate(String sDate, String sFmt) {
		if (sFmt.equals(DateUtil.DATE_FORMAT_DATETIME)) {  //  "YYYY/MM/DD HH24:MI:SS"
			return toDate(sDate, DateUtil.sdfDateTime);
		} else if (sFmt.equals (DateUtil.DATE_FORMAT_DATEONLY)) {  //YYYY/MM/DD
			return toDate(sDate, DateUtil.sdfDateOnly);
		} else {
			return null;
		}
	}

	/**
	 * SimpleDateFormat instance convert String to Date
	 *
	 * @param  sDate Date string
	 * @param  formatter  SimpleDateFormat instance
	 *
	 * @return Date
	 */
	private static java.util.Date toDate(String sDate, SimpleDateFormat formatter) {
		java.util.Date dt = null;
		try {
			dt = formatter.parse(sDate);
		} catch(Exception e) {
			e.printStackTrace();
			dt = null;
		}
		return dt;
	}

	/*****************************************************
	 * java.util.Date ==> String
	 *****************************************************/
	/**
	 * default Format(YYYY/MM/DD) to String
	 * @param  dt java.util.Date instance
	 * @return formatted string
	 */
	public static String toString(java.util.Date dt) {
		return toString(dt, DateUtil.sdfDateOnly);
	}

	/**
	 * convert java.util.Date to string accord to Format
	 * @param  dt java.util.Date instance
	 * @param  sFmt  Date format ,  DATE_FORMAT_DATEONLY or  DATE_FORMAT_DATETIME
	 * @return
	 */
	public static String toString(java.util.Date dt, String sFmt) {
		if (dt == null) {
			return null;
		}

		if (sFmt.equals (DateUtil.DATE_FORMAT_DATETIME)) {  //  "YYYY/MM/DD HH24:MI:SS"
			return toString(dt, DateUtil.sdfDateTime);
		} else {                                               //Default , YYYY/MM/DD
			return toString(dt, DateUtil.sdfDateOnly);
		}
	}

	/**
	 * convert java.util.Date to string accord to SimpleDateFormat
	 * @param  dt java.util.Date instance
	 * @param  formatter  SimpleDateFormat Instance
	 * @return
	 */
	private static String toString(java.util.Date dt, SimpleDateFormat formatter ) {
		String sRet = null;
		try {
			sRet = formatter.format(dt).toString();
		} catch(Exception e) {
			e.printStackTrace();
			sRet = null;
		}

		return sRet;
	}

	public static int[] DAY_OF_MONTH_LEAP_YEAR = {31,29,31,30,31,30,31,31,30,31,30,31};
	public static int[] DAY_OF_MONTH_NON_LEAP_YEAR = {31,28,31,30,31,30,31,31,30,31,30,31};

	/**
	 * Format year/month/day to YYYY/MM/DD format
	 * @param year
	 * @param month
	 * @param day
	 * @return  YYYY/MM/DD format String
	 */
	private static String formatYMD(int year,int month, int day) {
		String temp = String.valueOf(year) + "/";
		if (month < 10 ) {
			temp += "0" + String.valueOf(month) + "/";
		} else {
			temp += String.valueOf(month)+ "/";
		}
		if (day < 10 ) {
			temp += "0" + String.valueOf(day);
		} else {
			temp += String.valueOf(day);
		}
		return temp;
	}

	/*****************************************************
	 * get java.sql.Timestamp Object
	 *****************************************************/
	/** get Year value
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  year value
	 */
	public static int getYearOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return obj.get(GregorianCalendar.YEAR );
	}

	/**
	 * get month value
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  month value(1 -- 12 )
	 */
	public static int getMonthOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return (obj.get(GregorianCalendar.MONTH ) + 1);
	}

	/**
	 * get day value
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  day value
	 */
	public static int getDayOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return obj.get(GregorianCalendar.DAY_OF_MONTH);
	}

	/**
	 * get Hour value
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  0-23
	 */
	public static int getHourOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return obj.get(GregorianCalendar.HOUR_OF_DAY);
	}

	/**
	 * get minute value
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  minute value
	 */
	public static int getMinuteOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return obj.get(GregorianCalendar.MINUTE);
	}

	/**
	 * get second value
	 *
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  minute value
	 */
	public static int getSecondOfTimestamp(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = DateUtil.convertTimestampToCalendar(timestamp);
		return obj.get(GregorianCalendar.SECOND);
	}

	public static java.util.GregorianCalendar convertTimestampToCalendar(java.sql.Timestamp timestamp) {
		return convertToCalendar(timestamp);
	}

	public static java.util.GregorianCalendar convertToCalendar(java.sql.Timestamp timestamp) {
		GregorianCalendar obj = new GregorianCalendar();
		obj.setTime(DateUtil.convertTimestampToDate(timestamp));
		return obj;
	}

	/**
	 * java.sql.Timestamp Object to java.util.Date Object
	 * @param timestamp , java.sql.Timestamp Object
	 * @return  java.util.Date Object
	 */
	public static java.util.Date convertTimestampToDate(java.sql.Timestamp timestamp) {
		java.util.Date date = null;
		date = new Date(timestamp.getTime() );
		return date;
	}

	/*****************************************************
	 * get System date time
	 *****************************************************/
	/**
	 * long type sysdate
	 * @return  SYSDATE
	 */
	public static long getSysDateLong(){
		return System.currentTimeMillis();
	}

	/**
	 * java.sql.Timestamp type SYSDATE
	 * @return  java.sql.Timestamp SYSDATE
	 */
	public static java.sql.Timestamp getSysDateTimestamp(){
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * get YYYY/MM/DD System date String
	 * @return  Sysdate String
	 */
	public static String getSysDateString(){
		return toString(new java.util.Date(System.currentTimeMillis()),DateUtil.sdfDateOnly );
	}

	/**
	 * get YYYYMMDD or YYYY/MM/DD System date String
	 *
	 */
	public static String getSysDateString(boolean b) {
		String ret = getSysDateString();
		if (b) {
			return ret;
		} else {
			StringBuffer newString = new StringBuffer("");
			for (int i=0; i<ret.length(); i++) {
				if (ret.charAt(i) != '/')
					newString.append(ret.charAt(i));
			}
			return newString.toString();
		}
	}

	/**
	 * YYYY/MM/DD HH24:MI:SS System Datetime String
	 * @history
	 */
	public static String getSysDateTimeString() {
		return toString(new java.util.Date(System.currentTimeMillis()),DateUtil.sdfDateTime  );
	}

	/*****************************************************
	 * String ==> java.sql.Date
	 *****************************************************/
	/**
	 * java.sql.Date to String｣ｬYYYY/MM/DD
	 * @param  dt java.sql.Date instance
	 * @return
	 */
	public static String toSqlDateString(java.sql.Date dt) {
		String temp = null;
		temp = dt.toString();
		return temp.replace ('-','/');
	}

	/*****************************************************
	 * java.sql.Timestamp ==> String
	 *****************************************************/
	/**
	 * default (YYYY/MM/DD) String to java.sql.Timestamp
	 * @param  sDate Date string
	 * @return
	 */
	public static java.sql.Timestamp toSqlTimestamp( String sDate) throws Exception{
		if (sDate == null) return null;
		if(sDate.length() != DateUtil.DATE_FORMAT_DATEONLY.length() )
			return null;
		return toSqlTimestamp(sDate, DateUtil.DATE_FORMAT_DATEONLY );
	}

	/**
	 * (YYYY/MM/DD hh:mm:ss) String to java.sql.Timestamp
	 * @param  sDate Date string
	 * @param  sFmt Date format  DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME
	 * @return
	 */
	public static java.sql.Timestamp toSqlTimestamp(String sDate, String sFmt) throws Exception {
		String temp = null;
		if (sDate == null || sFmt == null) {
			return null;
		}

		if (sFmt.equals (DateUtil.DATE_FORMAT_DATETIME)) {
			if (sDate.length() == 10) {
				sDate = sDate + " 00:00:00";
			}
			temp = sDate.replace ('/', '-');
			temp = temp + ".000000000";
		} else if (sFmt.equals (DateUtil.DATE_FORMAT_DATEONLY)) {
			temp = sDate.replace ('/','-');
			temp = temp + " 00:00:00.000000000";
		} else if (sFmt.equals (DateUtil.DATE_FORMAT_TIMEONLY)) {
			temp = "1970/01/01 " + sDate + ".000000000";
			temp = temp.replace ('/','-');
		} else {
			return null;
		}

		//java.sql.Timestamp.value() format must be yyyy-mm-dd hh:mm:ss.fffffffff
		return java.sql.Timestamp.valueOf(temp);
	}

	public static java.sql.Date toSqlDate(String sDate) throws Exception {
		java.sql.Timestamp timeStamp = toSqlTimestamp(sDate, DateUtil.DATE_FORMAT_DATEONLY);

		if (timeStamp == null) {
			return null;
		}

		return new java.sql.Date(timeStamp.getTime());
	}

	public static java.sql.Time toSqlTime(String sDate) throws Exception {
		java.sql.Timestamp timeStamp = toSqlTimestamp(sDate, DateUtil.DATE_FORMAT_TIMEONLY);

		if (timeStamp == null) {
			return null;
		}

		return new java.sql.Time(timeStamp.getTime());
	}


   /**
	 * (YYYY/MM/DD hh:mm:ss) String to java.sql.Timestamp
	 * @param  sDate Date string
	 * @param  sFmt Date format  DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME
	 * @return
	 */
	public static java.sql.Timestamp toSqlTimestamp(java.util.Date dDate) throws Exception {

		String sDate;
		sDate = sFormat.format(dDate);

		return toSqlTimestamp(sDate, DateUtil.DATE_FORMAT_DATETIME);
	}



	/*****************************************************
	 * java.sql.Timestamp ==> String
	 *****************************************************/
	/**
	 * java.sql.Timestamp to String according format
	 * @param  dt java.sql.Timestamp instance
	 * @param  sFmt Date, DATE_FORMAT_DATEONLY/DATE_FORMAT_DATETIME
	 * @return
	 */
	public static String toSqlTimestampString(java.sql.Timestamp dt, String sFmt) {
		String temp = null;
		String out = null;
		if (dt == null || sFmt == null) return null;
		temp = dt.toString();
		 if (sFmt.equals(DateUtil.DATE_FORMAT_DATETIME) ||
			 	sFmt.equals(DateUtil.DATE_FORMAT_DATEONLY)) {
			temp = temp.substring(0, sFmt.length()) ;
			out = temp.replace ('-','/');
		} else if (sFmt.equals(DateUtil.DATE_FORMAT_TIMEONLY)) {
			temp = temp.substring(11, 11 + sFmt.length()) ;
			out = temp.replace ('-','/');
		}

		return out;
	}

	/**
	 * java.sql.Timestamp to Date String YYYY/MM/DD
	 *
	 */
	public static String getDateString(java.sql.Timestamp ts) {
		return toSqlTimestampString(ts, DateUtil.DATE_FORMAT_DATEONLY);
	}

	/**
	 * java.sql.Timestamp to Time String HH24:MI:SS
	 *
	 */
	public static String getTimeString(java.sql.Timestamp ts) {
		String temp = toSqlTimestampString(ts, DateUtil.DATE_FORMAT_DATETIME);
		return temp.substring(11);
	}

	/**
	 * 指定された日付の指定された日数後の日付を取得
	 * @param Date 日付
	 * @param int 日数
	 * @return Date 日付
	 * added by zhuyh on 2001/12/27
	 */
	public static Date getDateOfDis(java.util.Date d,int dis) {
		try{
			long time =d.getTime();
			time=time+DAY_MILLI*dis;
			return new Date(time);
		}
		catch(NullPointerException e){
			return null;
		}
	}

	/**
	 * 指定された日付の指定された日数後の日付を取得
	 * @param String 日付(yyyy/mm/dd)
	 * @param int 日数
	 * @return String 日付（yyyy/mm/dd）
	 *
	 */
	public static String getDateOfDis(String d,int dis) {
		try {
			java.util.Date dt = toDate( d, DATE_FORMAT_DATEONLY ) ;
			return toString(getDateOfDis(dt, dis ), DATE_FORMAT_DATEONLY );
		} catch(NullPointerException e){
			return null;
		}
	}

}
