package ro.unibuc.transitmate.registration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.unibuc.transitmate.user.User;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String form() {
        return "register";
    }

    @PostMapping
    public String register(@RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String password,
                           @RequestParam(defaultValue = "false") boolean consent,
                           Model model) {
        try {
            User user = registrationService.register(new RegistrationRequest(email, password, consent));
            model.addAttribute("email", user.getEmail());
            return "register-success";
        } catch (RegistrationException e) {
            model.addAttribute("error", e.getError().getMessage());
            model.addAttribute("email", email);
            return "register";
        }
    }
}
