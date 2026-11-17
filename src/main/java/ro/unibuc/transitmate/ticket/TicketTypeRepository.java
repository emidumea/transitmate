package ro.unibuc.transitmate.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByActiveTrueOrderByPriceRonAsc();
}
