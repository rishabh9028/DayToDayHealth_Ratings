package com.daytodayhealth.rating.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


    static SimpleDateFormat twentyFourHours = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat twelveHours = new SimpleDateFormat("hh:mm a");

    public static String twelveHourToTwentyFourHourConvert(String time) throws ParseException {
        Date date = twelveHours.parse(time);
        return twentyFourHours.format(date);
    }

    public static String twentyFourHourToTwelveHourConvert(String time) throws ParseException {
        Date date = twentyFourHours.parse(time);
        return twelveHours.format(date);
    }

    public static String getDayToDate(String dayFormate) {
        return new SimpleDateFormat(dayFormate).format(new Date());
    }

//	public static void main(String[] args) throws Exception {
//		System.out.println(twelveHourToTwentyFourHourConvert("10:30 PM"));
//		System.out.println(twentyFourHourToTwelveHourConvert("08:00:00"));
//	}

}
