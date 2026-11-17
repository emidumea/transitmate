package ro.unibuc.transitmate.ticket;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TicketPricingTest {

    @Test
    void halfPriceForFiftyPercentDiscount() {
        assertThat(TicketPricing.studentPrice(new BigDecimal("80.00"), 50)).isEqualByComparingTo("40.00");
    }

    @Test
    void noDiscountKeepsThePrice() {
        assertThat(TicketPricing.studentPrice(new BigDecimal("3.00"), 0)).isEqualByComparingTo("3.00");
    }

    @Test
    void roundsToTwoDecimals() {
        assertThat(TicketPricing.studentPrice(new BigDecimal("8.00"), 33)).isEqualByComparingTo("5.36");
    }

    @Test
    void rejectsDiscountOutsideZeroToHundred() {
        assertThatThrownBy(() -> TicketPricing.studentPrice(new BigDecimal("8.00"), 120))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void formatsPricesInRon() {
        assertThat(TicketPricing.formatRon(new BigDecimal("3.00"))).isEqualTo("3,00 RON");
    }
}
