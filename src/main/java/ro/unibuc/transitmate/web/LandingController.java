package ro.unibuc.transitmate.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.unibuc.transitmate.security.LandingPages;
import ro.unibuc.transitmate.user.Role;

@Controller
public class LandingController {

    @GetMapping("/home")
    public String home(Authentication authentication) {
        Role role = LandingPages.roleOf(authentication.getAuthorities());
        return "redirect:" + LandingPages.pathFor(role);
    }

    @GetMapping("/rider")
    public String rider(Authentication authentication, Model model) {
        return dashboard(authentication, model, Role.RIDER);
    }

    @GetMapping("/driver")
    public String driver(Authentication authentication, Model model) {
        return dashboard(authentication, model, Role.DRIVER);
    }

    @GetMapping("/inspector")
    public String inspector(Authentication authentication, Model model) {
        return dashboard(authentication, model, Role.INSPECTOR);
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        return dashboard(authentication, model, Role.ADMIN);
    }

    private String dashboard(Authentication authentication, Model model, Role role) {
        model.addAttribute("email", authentication.getName());
        model.addAttribute("roleLabel", LandingPages.labelFor(role));
        return "dashboard";
    }
}
