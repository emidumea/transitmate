package ro.unibuc.transitmate.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {

    private static final Instant NOW = Instant.parse("2026-11-17T10:00:00Z");

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        passwordEncoder = new BCryptPasswordEncoder();
        service = new RegistrationService(userRepository, passwordEncoder, Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void registersRiderWithHashedPasswordAndRecordedConsent() {
        User user = service.register(new RegistrationRequest("  Ana.Pop@Example.com ", "parola123", true));

        assertThat(user.getEmail()).isEqualTo("ana.pop@example.com");
        assertThat(user.getRole()).isEqualTo(Role.RIDER);
        assertThat(user.getPasswordHash()).isNotEqualTo("parola123");
        assertThat(passwordEncoder.matches("parola123", user.getPasswordHash())).isTrue();
        assertThat(user.getConsentAt()).isEqualTo(NOW);
        assertThat(user.getPrivacyPolicyVersion()).isEqualTo(RegistrationService.PRIVACY_POLICY_VERSION);
        verify(userRepository).save(user);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ana", "ana@", "@example.com", "ana pop@example.com", "ana@example"})
    void rejectsInvalidEmail(String email) {
        RegistrationError error = errorFor(new RegistrationRequest(email, "parola123", true));

        assertThat(error).isEqualTo(RegistrationError.INVALID_EMAIL);
        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsPasswordShorterThan8Characters() {
        RegistrationError error = errorFor(new RegistrationRequest("ana.pop@example.com", "parola1", true));

        assertThat(error).isEqualTo(RegistrationError.PASSWORD_TOO_SHORT);
        verify(userRepository, never()).save(any());
    }

    @Test
    void acceptsPasswordWithExactly8Characters() {
        User user = service.register(new RegistrationRequest("ana.pop@example.com", "parola12", true));

        assertThat(user.getEmail()).isEqualTo("ana.pop@example.com");
    }

    @Test
    void rejectsRegistrationWithoutConsent() {
        RegistrationError error = errorFor(new RegistrationRequest("ana.pop@example.com", "parola123", false));

        assertThat(error).isEqualTo(RegistrationError.CONSENT_REQUIRED);
        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsEmailAlreadyRegisteredIgnoringCase() {
        when(userRepository.existsByEmailIgnoreCase("ana.pop@example.com")).thenReturn(true);

        RegistrationError error = errorFor(new RegistrationRequest("ANA.POP@example.com", "parola123", true));

        assertThat(error).isEqualTo(RegistrationError.DUPLICATE_EMAIL);
        verify(userRepository, never()).save(any());
    }

    private RegistrationError errorFor(RegistrationRequest request) {
        try {
            service.register(request);
        } catch (RegistrationException e) {
            return e.getError();
        }
        return fail("Se astepta RegistrationException pentru " + request);
    }
}
