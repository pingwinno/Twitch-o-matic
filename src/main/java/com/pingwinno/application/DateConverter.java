package com.pingwinno.application;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {

public static String convert(String stringDate) {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String unixTime = null;
    try {
        Date  date = parser.parse(stringDate);
        unixTime =Long.toString(date.getTime());

    } catch (ParseException e) {
        e.printStackTrace();
    }
    return unixTime ;
}

    public static String convert(LocalDateTime localDateTime) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        return localDateTime.format(parser);
    }

}

