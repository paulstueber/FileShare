package de.paulstueber.media.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Configuration class with all configurations & beans concerning the entire application
 */
@Configuration
public class AppConfig {

    /**
     * Used to convert different properties to data models (e.g. comma separated properties to collection)
     * @return conversion service
     */
    @Bean
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

    /**
     * Bean for default object mapper with some configuration params set
     *
     * @return ObjectMapper instance
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // https://github.com/FasterXML/jackson-datatype-jsr310/issues/39#issuecomment-190206661
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(@NonNull final LocalDateTime localDateTime,
                                  @NonNull final JsonGenerator jsonGenerator,
                                  @NonNull final SerializerProvider serializerProvider)
                    throws IOException {
                ZonedDateTime zdt = localDateTime.atZone(ZoneOffset.UTC); //you might use a different zone
                jsonGenerator.writeString(DateTimeFormatter.ISO_INSTANT.format(zdt));
            }
        });
        objectMapper.registerModule(module);

        module = new SimpleModule();
        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(@NonNull final LocalDate localDate,
                                  @NonNull final JsonGenerator jsonGenerator,
                                  @NonNull final SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
            }
        });
        objectMapper.registerModule(module);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }
}
