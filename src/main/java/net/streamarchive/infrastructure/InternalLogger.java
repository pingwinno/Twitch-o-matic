package net.streamarchive.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Arrays;

public class InternalLogger implements Logger {
    private org.slf4j.Logger log;
    private String path;

    public InternalLogger(Class aClass, String path) {
        log = LoggerFactory.getLogger(aClass.getName());
        this.path = path;

    }

    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        log.trace(s);
        writeLog(s);
    }

    @Override
    public void trace(String s, Object o) {
        log.trace(s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        log.trace(s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void trace(String s, Object... objects) {
        log.trace(s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void trace(String s, Throwable throwable) {
        log.trace(s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        log.trace(marker, s);
        writeLog(s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        log.trace(marker, s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        log.trace(marker, s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        log.trace(marker, s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        log.trace(marker, s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(s);
        writeLog(s);
    }

    @Override
    public void debug(String s, Object o) {
        log.debug(s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        log.debug(s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(marker, s);
        writeLog(s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        log.debug(marker, s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        log.debug(marker, s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        log.debug(marker, s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(marker, s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(s);
        writeLog(s);
    }

    @Override
    public void info(String s, Object o) {
        log.info(s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void info(String s, Object o, Object o1) {
        log.info(s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(marker, s);
        writeLog(s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        log.info(marker, s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        log.info(marker, s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        log.info(marker, s, objects);
        writeLog(String.format(replacePlaceholder(s), Arrays.toString(objects)));
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(marker, s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(s);
        writeLog(s);
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        log.warn(s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1));
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(marker, s);
        writeLog(s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        log.warn(marker, s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        log.warn(marker, s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        log.warn(marker, s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(marker, s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(s);
        writeLog(s);
    }

    @Override
    public void error(String s, Object o) {
        log.error(s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void error(String s, Object o, Object o1) {
        log.error(s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        log.error(marker, s);
        writeLog(s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        log.error(marker, s, o);
        writeLog(String.format(replacePlaceholder(s), o.toString()));
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        log.error(marker, s, o, o1);
        writeLog(String.format(replacePlaceholder(s), o.toString(), o1.toString()));
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        log.error(marker, s, objects);
        writeLog(String.format(replacePlaceholder(s), objects));
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(marker, s, throwable);
        writeLog(String.format(replacePlaceholder(s), throwable.toString()));
    }

    private String replacePlaceholder(String s) {
        return s.replaceAll("\\{}", "%s");
    }

    private void writeLog(String s) {

        try {
            FileWriter fileWriter = new FileWriter(path + "/log.log", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(Instant.now() + ":  " + s);
            printWriter.close();
        } catch (IOException e) {
            log.error("Can't write log");
        }
    }
}
