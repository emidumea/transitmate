package ro.unibuc.transitmate.ticket;

/**
 * Un tip de bilet asa cum este afisat calatorului in catalog (US-17).
 */
public record TicketOffer(String name,
                          String categoryLabel,
                          String priceLabel,
                          String validity,
                          String studentDiscountRule) {

    static TicketOffer from(TicketType type) {
        String discountRule = type.getStudentDiscountPercent() > 0
                ? type.getStudentDiscountPercent() + "% reducere pentru studenti: "
                + TicketPricing.formatRon(TicketPricing.studentPrice(type.getPriceRon(), type.getStudentDiscountPercent()))
                : "Fara reducere";
        return new TicketOffer(
                type.getName(),
                type.getCategory().getLabel(),
                TicketPricing.formatRon(type.getPriceRon()),
                type.getValidity(),
                discountRule);
    }
}
