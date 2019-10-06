package spittr.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spittr.data.SpittleRepository;

//не связана со Spring MVC, это аннотация Spring CORE, не Spring MVC
//имеет тот же эффект, что аннотация @Component
@Controller
@RequestMapping({"/", "/homepage"})
public class HomeController {
    private SpittleRepository repository;

    //if i not specify method, all requests will be handled by this method
    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "home";
    }
}
