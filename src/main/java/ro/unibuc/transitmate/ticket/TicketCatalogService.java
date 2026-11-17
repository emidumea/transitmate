package ro.unibuc.transitmate.ticket;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketCatalogService {

    private final TicketTypeRepository ticketTypeRepository;

    public TicketCatalogService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketOffer> activeOffers() {
        return ticketTypeRepository.findByActiveTrueOrderByPriceRonAsc().stream()
                .map(TicketOffer::from)
                .toList();
    }
}
