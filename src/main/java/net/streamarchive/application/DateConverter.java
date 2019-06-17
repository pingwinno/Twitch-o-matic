package net.streamarchive.application;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {

    public static Date convert(String stringDate) {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
    try {
        date = parser.parse(stringDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }

        return date;
}

    public static String convert(LocalDateTime localDateTime) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        return localDateTime.format(parser);
    }

}

