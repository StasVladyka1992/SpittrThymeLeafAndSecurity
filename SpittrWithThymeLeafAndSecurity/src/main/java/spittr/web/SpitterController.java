package spittr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spittr.Spitter;
import spittr.data.SpitterRepository;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/spitter")
public class SpitterController {
    private SpitterRepository spitterRepository;

    @Autowired
    public SpitterController(SpitterRepository spitterRepository) {
        this.spitterRepository = spitterRepository;
    }

    public SpitterController() {
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        //by default attribute name will be the same, as name of the class
        //i added spitter attribute in order to populate this attribute on jsp
        //in form
        model.addAttribute("spitter", new Spitter());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    //If there will be some errors with validation, errors won't be empty
    //It is also important to put Errors errors RIGHT AFTER parameter with @Valid
    //This method can also process requests with pictures because of @RequestPart.
    //If for @Request part Part class is using, i don't need to config StandardServletMultipartResolver
    public String processRegistration(@Valid Spitter spitter,
                                      @RequestPart("profilePicture") MultipartFile profilePicture,
                                      Errors errors, RedirectAttributes model) throws IOException {
        if (errors.hasErrors()) {
            return "registerForm";
        }
        profilePicture.transferTo(new File(profilePicture.getOriginalFilename()));
        spitterRepository.save(spitter);
        // When InternalResourceViewResolver sees the redirect: prefix on the view specification,
        // it knows to interpret it as a redirect specification instead of as a view name.
        // InternalViewResolver also recognize "forward:" prefix

        //There is 2 options how we can redirect to the other page:
        //The first one is to type the redirect url, by it isn't safe
        //when constructiong URL and SQL queries
        //1) return "redirect:/spitter/" + spitter.getUsername();
        //It is better to do like this:
        model.addAttribute("username", spitter.getUsername());
        // spitter id isn't mapped to any URL placeholder, so it't tacked on to the redirect automatically
        // as a query parameter and the URL will be: /spitter/myusername?spitterId=42.
        model.addFlashAttribute("spitter", spitter);
        //add flash attribute is an opportunity to add attributes to the model during redirect.
        //flash attributes are added to the session and deleted in a request. RedirectAttributes is a sub-interface
        //of the model
        return "redirect:/spitter/{username}";
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showSpitterProfile (@PathVariable String username, Model model) {
        if (!model.containsAttribute("spitter")) {
            model.addAttribute(
                    spitterRepository.findByUserName(username));
        }
        return "profile";
    }
}
