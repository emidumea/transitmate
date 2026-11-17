package ro.unibuc.transitmate.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_types")
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketCategory category;

    @Column(name = "price_ron", nullable = false, precision = 8, scale = 2)
    private BigDecimal priceRon;

    @Column(nullable = false, length = 120)
    private String validity;

    @Column(name = "student_discount_percent", nullable = false)
    private int studentDiscountPercent;

    @Column(nullable = false)
    private boolean active;

    protected TicketType() {
        // cerut de JPA
    }

    public TicketType(String name, TicketCategory category, BigDecimal priceRon, String validity,
                      int studentDiscountPercent) {
        this.name = name;
        this.category = category;
        this.priceRon = priceRon;
        this.validity = validity;
        this.studentDiscountPercent = studentDiscountPercent;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public BigDecimal getPriceRon() {
        return priceRon;
    }

    public String getValidity() {
        return validity;
    }

    public int getStudentDiscountPercent() {
        return studentDiscountPercent;
    }

    public boolean isActive() {
        return active;
    }
}
