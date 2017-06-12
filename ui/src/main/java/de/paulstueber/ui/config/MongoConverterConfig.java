package de.paulstueber.ui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Collection of converters to serialize LocalDate(Time) objects. Needed by mongoDb to support
 * the serialisation of these types.
 * Details: http://stackoverflow.com/questions/22972679/spring-data-mongodb-with-java-8-localdate-mappingexception
 *
 * The dates are serialized using the internal .toString(obj) or .parse(str) methods.
 * Example serialisation: 2000-01-01T01:01:01
 */
public final class MongoConverterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConverterConfig.class);

    /**
     * hidden default constructor
     */
    private MongoConverterConfig() { }

    /**
     * Initialization of custom converters for MongoDB - needed to correctly (de)serialize java 8 local date time
     *
     * @return CustomConversions object containing all date-time converters neccessary
     */
    public static CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(
                new MongoConverterConfig.LocalDateToStringConverter(),
                new MongoConverterConfig.StringToLocalDateConverter(),
                new MongoConverterConfig.LocalDateTimeToStringConverter(),
                new MongoConverterConfig.StringToLocalDateTimeConverter(),
                new MongoConverterConfig.StringToUri(),
                new MongoConverterConfig.UriToString()));
    }

    /**
     * Used for mongoDb CustomConversions
     */
    public static class StringToUri implements Converter<String, URI> {
        @Override
        public URI convert(final String uri) {
            try {
                return uri != null ? new URI(uri) : null;
            } catch (URISyntaxException e) {
                LOGGER.warn("Invalid URI: {} - return null", uri);
            }
            return null;
        }
    }

    /**
     * Used for mongoDb CustomConversions
     */
    public static class UriToString implements Converter<URI, String> {
        @Override
        public String convert(final URI uri) {
            return uri != null ? uri.toString() : null;
        }
    }

    /**
     * Used for mongoDb CustomConversions
     *
     * Converter to deserialize LocalDateTime objects from String.
     * Example: 2000-01-01T01:01:01 -> LocalDateTime(2000, 01, 01, 01, 01, 01)
     */
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(final String date) {
            return date == null ? null : LocalDateTime.parse(date);
        }
    }

    /**
     * Used for mongoDb CustomConversions
     *
     * Converter to deserialize LocalDate objects from String.
     * Example: 2000-01-01 -> LocalDateTime(2000, 01, 01)
     */
    public static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(final String date) {
            return date == null ? null : LocalDate.parse(date);
        }
    }

    /**
     * Used for mongoDb CustomConversions
     *
     * Converter to serialize LocalDateTime objects to String.
     * Example: LocalDateTime(2000, 01, 01, 01, 01, 01) -> 2000-01-01T01:01:01
     */
    public static class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(final LocalDateTime localDateTime) {
            return localDateTime == null ? null : localDateTime.toString();
        }
    }

    /**
     * Used for mongoDb CustomConversions
     *
     * Converter to serialize LocalDateTime objects to String.
     * Example: LocalDateTime(2000, 01, 01) -> 2000-01-01
     */
    public static class LocalDateToStringConverter implements Converter<LocalDate, String> {
        @Override
        public String convert(final LocalDate localDate) {
            return localDate == null ? null : localDate.toString();
        }
    }
}
