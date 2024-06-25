package com.bodystats.api.config;

import org.springframework.core.convert.converter.Converter;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringToSqlTimeConverter implements Converter<String, Time> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Override
    public Time convert(String source) {
        try {
            return new Time(dateFormat.parse(source).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid time format. Please use HH:mm");
        }
    }
}
