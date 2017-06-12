package de.paulstueber.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Spring controller for Swagger Documentation.
 */
@Controller
@RequestMapping("/api/docs")
public class SwaggerController {

    /**
     * Simple redirect to the swagger ui by calling /docs instead.
     * @return the redirect
     */
    @RequestMapping(method = RequestMethod.GET)
    public String redirectSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
