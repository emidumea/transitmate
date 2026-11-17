package ro.unibuc.transitmate.registration;

public class RegistrationException extends RuntimeException {

    private final RegistrationError error;

    public RegistrationException(RegistrationError error) {
        super(error.getMessage());
        this.error = error;
    }

    public RegistrationError getError() {
        return error;
    }
}
