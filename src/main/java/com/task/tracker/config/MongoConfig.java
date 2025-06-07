package com.task.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.sql.Date;
import java.util.Arrays;

@Configuration
public class MongoConfig {

    private static class SqlDateToUtilDateConverter implements Converter<Date, java.util.Date> {
        @Override
        public java.util.Date convert(Date source) {
            return new java.util.Date(source.getTime());
        }
    }

    private static class UtilDateToSqlDateConverter implements Converter<java.util.Date, Date> {
        @Override
        public Date convert(java.util.Date source) {
            return new Date(source.getTime());
        }
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new SqlDateToUtilDateConverter(),
                new UtilDateToSqlDateConverter()
        ));
    }
}