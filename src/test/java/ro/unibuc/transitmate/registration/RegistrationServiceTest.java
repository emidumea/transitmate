package ro.unibuc.transitmate.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
}
