package ro.unibuc.transitmate.registration;

public enum RegistrationError {
    INVALID_EMAIL("Adresa de email nu este valida."),
    PASSWORD_TOO_SHORT("Parola trebuie sa aiba cel putin 8 caractere."),
    CONSENT_REQUIRED("Trebuie sa accepti prelucrarea datelor personale pentru a crea contul."),
    DUPLICATE_EMAIL("Exista deja un cont cu aceasta adresa de email.");

    private final String message;

    RegistrationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
