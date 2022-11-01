package com.bob.cors1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorsController {

    @GetMapping(path = "")
    public String index() {
        return "index";
    }
}
