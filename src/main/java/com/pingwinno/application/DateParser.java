package com.pingwinno.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class DateParser {

public static Date parse(String stringDate) {
    Date date = null;
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    try {
        date = parser.parse(stringDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return date;
}
public static String getFormattedDate(Date date){
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    return formatter.format(date);
}

}

