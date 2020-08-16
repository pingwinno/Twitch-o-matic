package net.streamarchive.util;

public class UrlFormatter {
    public static String format(String ... urlParts){
        return String.join("/",urlParts);
    }
}
