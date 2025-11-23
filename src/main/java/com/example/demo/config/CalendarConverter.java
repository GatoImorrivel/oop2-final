package com.example.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class CalendarConverter implements Converter<String, Calendar> {

    @Override
    public Calendar convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        try {
            // Handle ISO date format (YYYY-MM-DD)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(source));
            return calendar;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        }
    }
}
