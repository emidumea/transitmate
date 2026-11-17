package ro.unibuc.transitmate.registration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Clock;
import java.time.Instant;

@Service
public class RegistrationService {

    public static final String PRIVACY_POLICY_VERSION = "2026-10";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Transactional
    public User register(RegistrationRequest request) {
        String email = RegistrationRules.normalizeEmail(request.email());
        RegistrationRules.validate(email, request.password(), request.consent());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new RegistrationException(RegistrationError.DUPLICATE_EMAIL);
        }
        return userRepository.save(newRider(email, request.password()));
    }

    private User newRider(String email, String rawPassword) {
        Instant now = clock.instant();
        User user = new User(email, passwordEncoder.encode(rawPassword), Role.RIDER, now);
        user.recordConsent(now, PRIVACY_POLICY_VERSION);
        return user;
    }
}
