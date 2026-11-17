package ro.unibuc.transitmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Clock;

/**
 * Creeaza conturile de personal direct in baza de date, pana la implementarea US-32.
 */
@Component
public class StaffSeeder implements CommandLineRunner {

    public static final String DRIVER_EMAIL = "sofer@transitmate.ro";
    public static final String INSPECTOR_EMAIL = "controlor@transitmate.ro";
    public static final String ADMIN_EMAIL = "admin@transitmate.ro";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final String staffPassword;

    public StaffSeeder(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       Clock clock,
                       @Value("${transitmate.seed.staff-password}") String staffPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        this.staffPassword = staffPassword;
    }

    @Override
    public void run(String... args) {
        seed(DRIVER_EMAIL, Role.DRIVER);
        seed(INSPECTOR_EMAIL, Role.INSPECTOR);
        seed(ADMIN_EMAIL, Role.ADMIN);
    }

    private void seed(String email, Role role) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            userRepository.save(new User(email, passwordEncoder.encode(staffPassword), role, clock.instant()));
        }
    }
}
