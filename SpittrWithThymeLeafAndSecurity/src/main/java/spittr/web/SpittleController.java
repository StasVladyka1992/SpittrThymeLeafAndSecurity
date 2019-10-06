package spittr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import spittr.data.SpittleRepository;

@Controller
@RequestMapping("/spittles")
public class SpittleController {
    private static final String MAX_LONG_AS_STRING = "9223372036854775807";
    private SpittleRepository spittleRepository;

    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }

    public SpittleController() {
    }
    //    @RequestMapping(method=RequestMethod.GET)
//    public List<Spittle> spittles() {
//        return spittleRepository.findSpittles(Long.MAX_VALUE, 20));
//    }
//    When a handler method returns an object or a collection like this, the value returned is put
//    into the model, and the model key is inferred from its type (spittleList, as in the other examples).
//    As for the logical view name, it’s inferred from the request path. Because this
//    method handles GET requests for /spittles, the view name is spittles (chopping off
//    the leading slash).

    //    as a parameter here can be -  Map model
//     one more implementation of this method:
    @RequestMapping(method = RequestMethod.GET)
    public String spittles(@RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
                           @RequestParam(value = "count", defaultValue = "20") int count, Model model) {
        model.addAttribute("spittleList", spittleRepository.findSpittles(max, count));
        return "spittles";
    }

    //default value for PathVariable is the same as it's method parameter name
    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
    public String spittle(@PathVariable long spittleId, Model model) {
        model.addAttribute(spittleRepository.findOne(spittleId));
        return "spittle";
    }


}
