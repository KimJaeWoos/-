package com.jwoos.android.sellbook.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 컴대문 on 2016-07-14.
 */
public class TimeFomat {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static String list_time(Date tempDate) throws ParseException {


        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    public static String detail_time(String book_register_time) throws ParseException {
        //201605041129
        String time = book_register_time.substring(0,12);

        SimpleDateFormat original_format = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분");

        Date original_date = original_format.parse(time);
        String new_date = new_format.format(original_date);

        return new_date;
    }

    public static String comment_time(String book_register_time) throws ParseException {
        //201605041129
        String time = book_register_time.substring(0,12);

        SimpleDateFormat original_format = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy.MM.dd a hh:mm");

        Date original_date = original_format.parse(time);
        String new_date = new_format.format(original_date);

        return new_date;
    }

}

