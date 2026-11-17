package ro.unibuc.transitmate.registration;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Regulile de validare pentru crearea unui cont (US-01), separate de serviciu
 * ca sa poata fi citite si modificate intr-un singur loc.
 */
final class RegistrationRules {

    static final int MIN_PASSWORD_LENGTH = 8;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private RegistrationRules() {
    }

    static String normalizeEmail(String rawEmail) {
        return rawEmail == null ? "" : rawEmail.trim().toLowerCase(Locale.ROOT);
    }

    static void validate(String normalizedEmail, String password, boolean consent) {
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new RegistrationException(RegistrationError.INVALID_EMAIL);
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new RegistrationException(RegistrationError.PASSWORD_TOO_SHORT);
        }
        if (!consent) {
            throw new RegistrationException(RegistrationError.CONSENT_REQUIRED);
        }
    }
}
