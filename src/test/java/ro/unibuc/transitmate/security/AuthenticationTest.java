package ro.unibuc.transitmate.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.transitmate.config.StaffSeeder;
import ro.unibuc.transitmate.user.Role;
import ro.unibuc.transitmate.user.User;
import ro.unibuc.transitmate.user.UserRepository;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationTest {

    private static final String RIDER_EMAIL = "ana.pop@example.com";
    private static final String RIDER_PASSWORD = "parola123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${transitmate.seed.staff-password}")
    private String staffPassword;

    @BeforeEach
    void createRider() {
        userRepository.save(new User(RIDER_EMAIL, passwordEncoder.encode(RIDER_PASSWORD), Role.RIDER, Instant.now()));
    }

    @Test
    void loginPageIsPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void correctCredentialsLogTheRiderIn() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user(RIDER_EMAIL).password(RIDER_PASSWORD))
                .andExpect(authenticated().withUsername(RIDER_EMAIL).withRoles("RIDER"))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void emailIsNotCaseSensitiveAtLogin() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user("ANA.POP@example.com").password(RIDER_PASSWORD))
                .andExpect(authenticated().withUsername(RIDER_EMAIL));
    }

    @Test
    void wrongPasswordShowsAnError() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user(RIDER_EMAIL).password("gresita123"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void unknownEmailShowsTheSameError() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user("nimeni@example.com").password(RIDER_PASSWORD))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void logoutEndsTheSession() throws Exception {
        mockMvc.perform(logout())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void seededDriverCanLogInWithTheDriverRole() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user(StaffSeeder.DRIVER_EMAIL).password(staffPassword))
                .andExpect(authenticated().withRoles("DRIVER"));
    }

    @Test
    @WithMockUser(username = RIDER_EMAIL, roles = "RIDER")
    void homeSendsTheRiderToTheRiderPage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(redirectedUrl("/rider"));
    }

    @Test
    @WithMockUser(username = "admin@transitmate.ro", roles = "ADMIN")
    void homeSendsTheAdminToTheAdminPage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    @WithMockUser(username = "sofer@transitmate.ro", roles = "DRIVER")
    void driverSeesTheDriverDashboard() throws Exception {
        mockMvc.perform(get("/driver"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("roleLabel", "Sofer"));
    }

    @Test
    @WithMockUser(username = "controlor@transitmate.ro", roles = "INSPECTOR")
    void inspectorSeesTheInspectorDashboard() throws Exception {
        mockMvc.perform(get("/inspector"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roleLabel", "Controlor"));
    }

    @Test
    @WithMockUser(username = RIDER_EMAIL, roles = "RIDER")
    void riderDashboardShowsTheEmail() throws Exception {
        mockMvc.perform(get("/rider"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("email", RIDER_EMAIL));
    }

    @Test
    @WithMockUser(username = RIDER_EMAIL, roles = "RIDER")
    void riderCannotOpenTheAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousUserIsSentToLogin() throws Exception {
        mockMvc.perform(get("/rider"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
