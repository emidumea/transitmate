package ro.unibuc.transitmate.ticket;

public enum TicketCategory {
    SINGLE_TRIP("O calatorie"),
    DAY("O zi"),
    MONTHLY("Abonament lunar");

    private final String label;

    TicketCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
