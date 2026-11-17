package ro.unibuc.transitmate.ticket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketCatalogController {

    private final TicketCatalogService ticketCatalogService;

    public TicketCatalogController(TicketCatalogService ticketCatalogService) {
        this.ticketCatalogService = ticketCatalogService;
    }

    @GetMapping("/tickets")
    public String tickets(Model model) {
        model.addAttribute("offers", ticketCatalogService.activeOffers());
        return "tickets";
    }
}
