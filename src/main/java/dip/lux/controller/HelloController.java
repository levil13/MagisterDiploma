package dip.lux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(Model model) {
        model.addAttribute("title","Magister Diploma");
        return "index";
    }
}
