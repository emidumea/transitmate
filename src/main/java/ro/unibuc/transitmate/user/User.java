package ro.unibuc.transitmate.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "consent_at")
    private Instant consentAt;

    @Column(name = "privacy_policy_version", length = 20)
    private String privacyPolicyVersion;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected User() {
        // cerut de JPA
    }

    public User(String email, String passwordHash, Role role, Instant createdAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public void recordConsent(Instant consentAt, String privacyPolicyVersion) {
        this.consentAt = consentAt;
        this.privacyPolicyVersion = privacyPolicyVersion;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public Instant getConsentAt() {
        return consentAt;
    }

    public String getPrivacyPolicyVersion() {
        return privacyPolicyVersion;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
