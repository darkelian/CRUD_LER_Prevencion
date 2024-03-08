package com.crud.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping("/")
    public ModelAndView showIndex(Model model) {
        ModelAndView layout = new ModelAndView("index.html");
        layout.addObject("contenido", "pagina2");
        return layout;
    }
}
