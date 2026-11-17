package ro.unibuc.transitmate.registration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    public static final String PRIVACY_POLICY_VERSION = "2026-10";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

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
        String email = request.email() == null ? "" : request.email().trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RegistrationException(RegistrationError.INVALID_EMAIL);
        }
        if (request.password() == null || request.password().length() < 8) {
            throw new RegistrationException(RegistrationError.PASSWORD_TOO_SHORT);
        }
        if (!request.consent()) {
            throw new RegistrationException(RegistrationError.CONSENT_REQUIRED);
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new RegistrationException(RegistrationError.DUPLICATE_EMAIL);
        }
        Instant now = clock.instant();
        User user = new User(email, passwordEncoder.encode(request.password()), Role.RIDER, now);
        user.recordConsent(now, PRIVACY_POLICY_VERSION);
        return userRepository.save(user);
    }
}
