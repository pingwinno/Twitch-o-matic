package com.pingwinno.application;

import jdk.vm.ci.meta.Local;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

}

