package io.hexlet.hexletcorrection.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
@AllArgsConstructor
public class IndexController {

    private static final String INDEX = "index";

    @GetMapping
    public String index() {
        return INDEX;
    }
}
