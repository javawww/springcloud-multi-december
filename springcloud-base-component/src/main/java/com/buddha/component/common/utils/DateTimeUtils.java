package com.buddha.component.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间工具类
 * @作者 仝照美
 * @时间 2018年11月30日
 * @版权   深圳市佛系派互联网科技集团
 */
@Slf4j
public final class DateTimeUtils {

	/**
	 * 格式化为：两位年，例如：18
	 */
	public static final String FORMAT_YY = "yy";

	/**
	 * 格式化为：四位年，例如：2018
	 */
	public static final String FORMAT_YYYY = "yyyy";

	/**
	 * 格式化为：两位月，例如：11
	 */
	public static final String FORMAT_MM = "MM";

	/**
	 * 格式化为：两位日，例如：28
	 */
	public static final String FORMAT_DD = "dd";

	/**
	 * 格式化为：四位年-两位月，例如：2018-07
	 */
	public static final String FORMAT_YYYY_MM = "yyyy-MM";

	/**
	 * 格式化为：四位年-两位月-两位日，例如：2018-07-08
	 */
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 格式化为：四位年两位月，例如：201807
	 */
	public static final String FORMAT_YYYY_MM_S = "yyyyMM";

	/**
	 * 格式化为：四位年两位月两位日，例如：0708
	 */
	public static final String FORMAT_MM_DD_S = "MMdd";

	/**
	 * 格式化为：四位年两位月两位日，例如：20180708
	 */
	public static final String FORMAT_YYYY_MM_DD_S = "yyyyMMdd";

	/**
	 * 格式化为：四位年-两位月-两位日，例如：07-08
	 */
	public static final String FORMAT_MM_DD = "MM-dd";

	/**
	 * 格式化为：四位年-两位月-两位日 小时:分钟，例如：2018-07-08 15
	 */
	public static final String FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";

	/**
	 * 格式化为：四位年-两位月-两位日 小时:分钟，例如：2018-07-08 15:20
	 */
	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	
	public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";

	/**
	 * 格式化为：2018-06-15 12:30:50
	 */
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 格式化为：2018/06/15 12:30:50
	 */
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SLASH = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 格式化为：20180709182545
	 */
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_S = "yyyyMMddHHmmss";
	
	public static final String FORMAT_YY_MM_DD_HH = "yyMMddHH";

	/**
	 * 格式化为：四位年-两位月，例如：2018年10月
	 */
	public static final String FORMAT_YYYY_MM_CHINA = "yyyy年MM月";

	/**
	 * 格式化为：2018年07月10日
	 */
	public static final String FORMAT_YYYY_MM_DD_CHINA = "yyyy年MM月dd日";

	/**
	 * 格式化为：2018年12月12日 12时34分50秒
	 */
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_CHINA = "yyyy年MM月dd日 HH时mm分ss秒";
	
	/**
	 * 格式化为：7月30日 12:00
	 */
	public static final String FORMAT_MM_DD_HH_MM_DISH_SHARE_PIC = "MM月dd日 HH:mm";
	
	/**
	 * 格式化当前时间
	 * 
	 * @param pattern
	 *            格式化类型
	 * @return
	 */
	public static String getCurrentDateTimeToString(String pattern) {
		return getDateTimeFormatToString(new Date(), pattern);
	}

	/**
	 * 获取标准北京时间（从中科院获取）
	 * 
	 * @param pattern
	 *            格式化类型
	 * @return
	 */
	public static String getStandardBeiJingDateTime(String pattern) {
		try {
			String ntscUrl = "http://www.ntsc.ac.cn";
			// String baiduUrl = "http://www.baidu.com";
			URL url = new URL(ntscUrl);// 取得资源对象
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect();// 发出连接
			long ld = uc.getDate();// 读取网站日期时间
			Date date = new Date(ld);// 转换为标准时间对象
			return getDateTimeFormatToString(date, pattern);
		} catch (Exception e) {
//			log.error("获取标准北京时间异常", e);
		}
		return getDateTimeFormatToString(new Date(), pattern);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 *            日期对象
	 * @param pattern
	 *            转换格式
	 * @return
	 */
	public static String getDateTimeFormatToString(Date date, String pattern) {
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(pattern);
	}

	/**
	 * 
	 * 把字符串时间按照转换格式转换成日期对象
	 * 
	 * @param datetime
	 *            字符串时间
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static Date getStringDateTimeToDateTime(String datetime, String pattern) {
		DateTime dateTime = DateTimeFormat.forPattern(pattern).parseDateTime(datetime);
		return dateTime.toDate();
	}

	/**
	 * 日期计算<br />
	 * date为空表示使用当前时间
	 * 
	 * @param date
	 *            日期对象
	 * @param type
	 *            类型（使用Calendar类的常量定义，例如月份：Calendar.MONTH）
	 * @param value
	 *            值（正数：添加。负数：减去）
	 * @return
	 */
	public static Date reckonDate(Date date, int type, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date != null ? date : new Date());// 设置日期
		calendar.add(type, value);// 计算
		return calendar.getTime();
	}

	/**
	 * 日期计算
	 * 
	 * @param date
	 *            日期对象
	 * @param year
	 *            年（正数：添加。负数：减去）
	 * @param month
	 *            月（正数：添加。负数：减去）
	 * @param day
	 *            日（正数：添加。负数：减去）
	 * @return
	 */
	public static Date reckonDate(Date date, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date != null ? date : new Date());// 设置日期
		if (day != 0) {
			calendar.add(Calendar.DATE, day);
		}
		if (month != 0) {
			calendar.add(Calendar.MONTH, month);
		}
		if (year != 0) {
			calendar.add(Calendar.YEAR, year);
		}
		return calendar.getTime();
	}

	/**
	 * 比较两个日期的天数差（天）
	 * 
	 * @param beginDate
	 *            开始日期（包含年月日即可）
	 * @param endDate
	 *            结束日期（包含年月日即可）
	 * @return 返回两个日期相差的天数（0：表示同一天；负数表示结束日期小于开始时间）
	 */
	public static int getDateIntervalDay(Date beginDate, Date endDate) {
		try {
			int day = 0;
			day = (int) (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);
			return day;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 比较两个日期的天数差（天）
	 * 
	 * @param beginDateStr
	 *            开始日期字符串（格式：yyyy-MM-dd）
	 * @param endDateStr
	 *            结束日期字符串（格式：yyyy-MM-dd）
	 * @return 返回两个日期相差的天数（0：表示同一天；负数表示结束日期小于开始时间）
	 */
	public static int getDateIntervalDay(String beginDateStr, String endDateStr) {
		Date beginDate = getStringDateTimeToDateTime(beginDateStr, FORMAT_YYYY_MM_DD);
		Date endDate = getStringDateTimeToDateTime(endDateStr, FORMAT_YYYY_MM_DD);
		return getDateIntervalDay(beginDate, endDate);
	}

	/**
	 * 比较两个时间的分钟差（分钟）
	 * 
	 * @param beginDateTime
	 *            （包含年月日、时分） 开始时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @param endDateTime
	 *            （包含年月日、时分） 结束时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @return
	 */
	public static int getDateTimeIntervalMinute(Date beginDateTime, Date endDateTime) {
		try {
			int day = 0;
			day = (int) (endDateTime.getTime() - beginDateTime.getTime()) / (1000 * 60);
			return day;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 比较两个时间的时钟差（时钟差）
	 * 
	 * @param beginDateTime
	 *            （包含年月日、时分） 开始时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @param endDateTime
	 *            （包含年月日、时分） 结束时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @return
	 */
	public static int getDateTimeIntervalHour(Date beginDateTime, Date endDateTime) {
		try {
			int day = 0;
			day = (int) (endDateTime.getTime() - beginDateTime.getTime()) / (1000 * 60) / 60;
			return day;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 比较两个时间的分钟差（分钟）
	 * 
	 * @param beginDateTimeStr
	 *            开始时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @param endDateTimeStr
	 *            结束时间字符串（格式：yyyy-MM-dd HH:mm）
	 * @return
	 */
	public static int getDateTimeIntervalMinute(String beginDateTimeStr, String endDateTimeStr) {
		Date beginDate = getStringDateTimeToDateTime(beginDateTimeStr, FORMAT_YYYY_MM_DD_HH_MM);
		Date endDate = getStringDateTimeToDateTime(endDateTimeStr, FORMAT_YYYY_MM_DD_HH_MM);
		return getDateTimeIntervalMinute(beginDate, endDate);
	}

	/**
	 * 获取当月的最后一天
	 * 
	 * @param date
	 *            日期（包含有年月）
	 * @return
	 */
	public static Integer getLastDayOfMonth(Date date) {
		DateTime t = new DateTime(date);
		Calendar calendar = t.toCalendar(Locale.getDefault());
		int dayCount = calendar.getActualMaximum(Calendar.DATE);
		return dayCount;
	}

	/**
	 * 获取当月的最后一天
	 * 
	 * @param dateStr
	 *            日期（格式：yyyy-MM-dd）
	 * @return
	 */
	public static Integer getLastDayOfMonth(String dateStr) {
		Date date = getStringDateTimeToDateTime(dateStr, FORMAT_YYYY_MM_DD);
		return getLastDayOfMonth(date);
	}

	/**
	 * 通过生日获取年龄
	 * 
	 * @param birthDate
	 *            生日日期（包含年月日）
	 * @return
	 */
	public static Integer getAge(Date birthDate) {
		DateTime dateTime = new DateTime(birthDate);
		LocalDate birthday = dateTime.toLocalDate();
		LocalDate now = new LocalDate();
		Period period = new Period(birthday, now, PeriodType.yearMonthDay());
		return period.getYears();
	}

	/**
	 * 通过生日获取年龄
	 * 
	 * @param birthDateStr
	 *            生日日期（格式：yyyy-MM-dd）
	 * @return
	 */
	public static Integer getAge(String birthDateStr) {
		Date date = getStringDateTimeToDateTime(birthDateStr, FORMAT_YYYY_MM_DD);
		return getAge(date);
	}
	
	/**
	 * date2 比 date1多的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1,Date date2){
		Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        // 同一年
        if(year1 != year2){
        	int timeDistance = 0 ;
        	for(int i = year1 ; i < year2 ; i ++){
        		// 闰年
        		if(i%4==0 && i%100!=0 || i%400==0){
        			timeDistance += 366;
        		}else{
        			// 不是闰年
        			timeDistance += 365;
        		}
        	}
        	return timeDistance + (day2-day1) ;
        }else{
        	// 不同年
        	return day2-day1;
        }
	}
	
	/**
	 * date2 比 date1多的天数
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static String getDatePoor(Date endDate, Date startDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - startDate.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
	    return day + "天" + hour + "小时" + min + "分钟";
	}
	
	public static Float getDayAndHour(Date endDate, Date startDate) {
		float nd = 1000 * 24 * 60 * 60;
		float nh = 1000 * 60 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
		float diff = endDate.getTime() - startDate.getTime();
	    // 计算差多少天
		float day = diff / nd;
	    // 计算差多少小时
		float hour = diff % nd / nh;
		// 小时转换为天
		float hday = hour / 24 ;
		float finalday = 0f;
		if(day > 1f) {
			finalday = day + hday;
		}else {
			finalday = hday;
		}
		return finalday;
	}
	
	/**
	 * 获取多少秒之后的日期
	 * @param curDate
	 * @param seconds
	 * @return
	 */
	public static Date addSeconds(Date curDate,int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
	
	/**
	  * 得到几天前的时间
	  */

	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 *   * 得到几天后的时间  
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);

		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);

		return now.getTime();
	}

    /**
     * @MethodName: getodrInternal
     * @Description: 获取内部订单号规则
     * @Param: []
     * @Return: java.lang.String
     * @Author: LiuZW
     * @Date: 2019/8/15/015 15:03
     **/
	public static String getOdrInternal(){
	    //规则为：年月日时分秒 201908201220255
        String standardBeiJingDateTime = getStandardBeiJingDateTime(FORMAT_YYYY_MM_DD_HH_MM_SS_S);
        String randomNumberByLength = RandomUtil.getRandomNumberByLength(3);
        return standardBeiJingDateTime+randomNumberByLength;
    }
    /**
     * @MethodName: getPayScanInternal
     * @Description: 生辰12位核销码
     * @Param: []
     * @Return: java.lang.String
     * @Author: LiuZW
     * @Date: 2019/11/19/019 11:46
     **/
	public static String getPayScanInternal(){
	    //规则为：年月日时分秒 201908201220255
        String standardBeiJingDateTime = getStandardBeiJingDateTime(FORMAT_YY_MM_DD_HH);
        String randomNumberByLength = RandomUtil.getRandomNumberByLength(4);
        return standardBeiJingDateTime+randomNumberByLength;
    }

	public static void main(String[] args) {
        String payScanInternal = getPayScanInternal();
        System.out.println(payScanInternal);
		/*int seconds = 24 * 60 * 60; // 24小时
		Date curDate = new Date();
		Date afterDate = DateTimeUtils.addSeconds(curDate, seconds);
		System.out.println(curDate.toLocaleString());
		System.out.println(afterDate.toLocaleString());*/
/*
        String beginTime = "2018-07-28 14:42:31";
        String endTime = "2018-07-28 14:42:32";
        //String endTime = "2018-07-29 12:26:32";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date1 = format.parse(beginTime);
            Date date2 = format.parse(endTime);

            boolean before = date1.before(date2);

            System.out.println(before);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
//		Date date1= DateTimeUtils.getStringDateTimeToDateTime("2018-11-30 12:00", FORMAT_YYYY_MM_DD_HH_MM);
//		Date date2= DateTimeUtils.getStringDateTimeToDateTime("2018-11-30 12:00", FORMAT_YYYY_MM_DD_HH_MM);
//		Integer day = differentDays(date1, date2);
//		System.out.println(day);
//		String str = getDatePoor(date2, date1);
//		System.out.println(str);
//		Float finalday = getDayAndHour(date2, date1);
//		if(finalday < 0f) {
//			System.out.println("处理时间设置错误，不能小于开始时间");
//		}
//		if(finalday == 0f){
//			System.out.println("处理时间 == 开始时间");
//		}
//		// 保留两位小数
//		BigDecimal  bd   =   new  BigDecimal((double)finalday);   
//		bd   =   bd.setScale(2,4); 
//		finalday   =   bd.floatValue();
//		System.out.println(finalday);

    }
}
