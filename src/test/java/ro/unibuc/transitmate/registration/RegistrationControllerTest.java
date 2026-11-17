package ro.unibuc.transitmate.registration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.transitmate.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void showsRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void validRegistrationShowsConfirmationAndCreatesAccount() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("email", "maria@example.com")
                        .param("password", "parola123")
                        .param("consent", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-success"))
                .andExpect(model().attribute("email", "maria@example.com"));

        assertThat(userRepository.existsByEmailIgnoreCase("maria@example.com")).isTrue();
    }

    @Test
    void missingConsentShowsErrorAndKeepsEmail() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("email", "maria@example.com")
                        .param("password", "parola123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", RegistrationError.CONSENT_REQUIRED.getMessage()))
                .andExpect(model().attribute("email", "maria@example.com"));

        assertThat(userRepository.existsByEmailIgnoreCase("maria@example.com")).isFalse();
    }

    @Test
    void registrationWithoutCsrfTokenIsRejected() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "maria@example.com")
                        .param("password", "parola123")
                        .param("consent", "true"))
                .andExpect(status().isForbidden());
    }

    @Test
    void duplicateEmailShowsError() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("email", "ion@example.com")
                .param("password", "parola123")
                .param("consent", "true"));

        mockMvc.perform(post("/register").with(csrf())
                        .param("email", "ION@example.com")
                        .param("password", "altaparola")
                        .param("consent", "true"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", RegistrationError.DUPLICATE_EMAIL.getMessage()));
    }
}
