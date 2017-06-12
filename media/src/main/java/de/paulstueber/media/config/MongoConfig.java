package de.paulstueber.media.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

/**
 * Setup MongoDb: Springs default MongoAutoConfiguration does not easily support setting custom
 * converters. This configuration is needed to set them
 *
 */
@Configuration
class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.uri}")
    private String uri;

    /**
     * Initialization of custom converters for MongoDB - needed to correctly (de)serialize java 8 local date time
     *
     * @return CustomConversions object containing all date-time converters neccessary
     */
    @Override
    @Bean
    @Primary
    public CustomConversions customConversions() {
        return MongoConverterConfig.customConversions();
    }

    /**
     * return the name of the current database name (needed by parent class)
     *
     * @return database name
     */
    @Override
    protected String getDatabaseName() {
        return database;
    }

    /**
     * Initialize a new MongoClient with the current MongoDB host/port as parameter
     *
     * @return new mongo client
     * @throws UnknownHostException thrown on unknoen host
     */
    @Override
    @Bean
    public MongoClient mongo() throws UnknownHostException {
        return new MongoClient(new MongoClientURI(uri));
    }

    /**
     * Mongo converter as been to ensure correct autowireing by mongo template
     *
     * @return mongo mapping converter
     * @throws Exception thrown by monto db factory
     */
    @Bean
    @Primary
    public MappingMongoConverter mongoConverter() throws Exception {
        MongoMappingContext mappingContext = new MongoMappingContext();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mongoConverter.setCustomConversions(customConversions());
        mongoConverter.afterPropertiesSet();
        return mongoConverter;
    }

    /**
     * Initilize a new MongoTemplate with the current MongoClient and Database name
     *
     * @return new mongo template
     * @throws Exception thrown by monto db factory
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), mongoConverter());
        return mongoTemplate;
    }
}
