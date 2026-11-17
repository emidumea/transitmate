package ro.unibuc.transitmate.registration;

public record RegistrationRequest(String email, String password, boolean consent) {
}
