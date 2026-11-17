package ro.unibuc.transitmate.ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Calculele de pret pentru bilete: reducerea de student si afisarea in RON.
 */
public final class TicketPricing {

    private static final Locale ROMANIAN = Locale.forLanguageTag("ro-RO");
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private TicketPricing() {
    }

    public static BigDecimal studentPrice(BigDecimal price, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Reducerea trebuie sa fie intre 0 si 100%");
        }
        BigDecimal payablePercent = ONE_HUNDRED.subtract(BigDecimal.valueOf(discountPercent));
        return price.multiply(payablePercent).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static String formatRon(BigDecimal amount) {
        return String.format(ROMANIAN, "%.2f RON", amount);
    }
}
