package de.paulstueber.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller mapping all static views to their resources
 */
@Controller
@RequestMapping("/")
public class ViewController {

    /**
     * The start/index page available to any visitor
     * @return
     */
    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    public String index(final Model model) {
        model.addAttribute("currentMillies", System.currentTimeMillis());
        return "/index";
    }


    /**
     * Upload endpoint returning a string defining the thymeleaf template
     * @return
     */
    @RequestMapping("/upload")
    public String upload() {
        return "/upload";
    }
}
