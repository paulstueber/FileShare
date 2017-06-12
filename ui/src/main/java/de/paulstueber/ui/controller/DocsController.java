package de.paulstueber.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Redirect controller to swagger over /docs
 */
@Controller
@RequestMapping("/docs")
public class DocsController {

    /**
     * Redirect route /docs to /swagger-ui.html for simplicity reasons
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String redirectSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
