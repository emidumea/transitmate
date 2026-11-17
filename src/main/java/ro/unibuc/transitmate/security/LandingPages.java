package ro.unibuc.transitmate.security;

import org.springframework.security.core.GrantedAuthority;
import ro.unibuc.transitmate.user.Role;

import java.util.Collection;

/**
 * Ecranul principal al fiecarui rol dupa autentificare (US-02).
 */
public final class LandingPages {

    private static final String ROLE_PREFIX = "ROLE_";

    private LandingPages() {
    }

    public static String pathFor(Role role) {
        return switch (role) {
            case RIDER -> "/rider";
            case DRIVER -> "/driver";
            case INSPECTOR -> "/inspector";
            case ADMIN -> "/admin";
        };
    }

    public static String labelFor(Role role) {
        return switch (role) {
            case RIDER -> "Calator";
            case DRIVER -> "Sofer";
            case INSPECTOR -> "Controlor";
            case ADMIN -> "Admin operator";
        };
    }

    public static Role roleOf(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(ROLE_PREFIX))
                .map(authority -> Role.valueOf(authority.substring(ROLE_PREFIX.length())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Utilizatorul autentificat nu are niciun rol"));
    }
}
