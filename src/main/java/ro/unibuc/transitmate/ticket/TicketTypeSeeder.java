package ro.unibuc.transitmate.ticket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Tipurile de bilete initiale (date simulate). Adminul le va putea modifica prin US-31.
 */
@Component
public class TicketTypeSeeder implements CommandLineRunner {

    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeSeeder(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public void run(String... args) {
        if (ticketTypeRepository.count() > 0) {
            return;
        }
        ticketTypeRepository.save(new TicketType("Bilet o calatorie", TicketCategory.SINGLE_TRIP,
                new BigDecimal("3.00"), "90 de minute de la validare", 0));
        ticketTypeRepository.save(new TicketType("Bilet o zi", TicketCategory.DAY,
                new BigDecimal("8.00"), "pana la sfarsitul zilei in care a fost cumparat", 0));
        ticketTypeRepository.save(new TicketType("Abonament lunar", TicketCategory.MONTHLY,
                new BigDecimal("80.00"), "30 de zile de la cumparare", 50));
    }
}
