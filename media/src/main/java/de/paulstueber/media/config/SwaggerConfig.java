package de.paulstueber.media.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Swag-it, babe!
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static List<String> activatedPaths = new ArrayList<>();

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private ApplicationContext appContext;

    /**
     * Bean to initialize SpringFox Docket to enable/hide various Rest Endpoints for swagger
     * @return a docket
     */
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.paulstueber.media.controller"))
                .paths(this::matchWithMappingValues)
                .build()
                .apiInfo(this.apiInfo());
    }

    /**
     * Configure the swagger html
     * @return
     */
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(serviceName + ": Api Documentation")
                .build();
        return apiInfo;
    }

    /**
     * Iterate over all RestController beans that hold an RequestMapping annotation
     * and add all RequestMapping values (paths) to the activatedPaths list.
     *
     * activatedPaths is used to signalize that all paths in that list are part of the public API
     */
    @PostConstruct
    private void initActivatedPaths() {
        Map<String, Object> beans = appContext.getBeansWithAnnotation(RestController.class);

        beans.values().stream()
                .map(o -> o.getClass().getAnnotation(RequestMapping.class).value())
                .forEach(value -> activatedPaths.addAll(Arrays.asList(value)));
    }

    /**
     * Compare a given path with all RequestMapping values of classed annotated with @RestController.
     * If the path starts with any of the mapping values, return true to signalize that the path is
     * part of the public API
     *
     * Example:
     * path : /mediaobjects/videoflip/{videoflipId}
     * match: /mediaobjects (of class MediaObjectController)
     *
     *
     * @param path the path
     * @return boolean if a custom controller mapping matches the path
     */
    private boolean matchWithMappingValues(final String path) {
        /*
         * returns true if a class is found that has an RequestMapping annotation that forms the beginning
         * of the path string
         */
        return activatedPaths.stream().anyMatch(path::startsWith);
    }

}
