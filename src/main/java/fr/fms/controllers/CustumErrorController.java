package fr.fms.controllers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustumErrorController implements ErrorController {
    private static final String PATH = "/error";
    @RequestMapping(value = PATH)
    public String handleError() {
        return "/404";
    }
}
