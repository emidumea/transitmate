package ro.unibuc.transitmate.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ro.unibuc.transitmate.user.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LandingPagesTest {

    @Test
    void eachRoleHasItsOwnHomePage() {
        assertThat(LandingPages.pathFor(Role.RIDER)).isEqualTo("/rider");
        assertThat(LandingPages.pathFor(Role.DRIVER)).isEqualTo("/driver");
        assertThat(LandingPages.pathFor(Role.INSPECTOR)).isEqualTo("/inspector");
        assertThat(LandingPages.pathFor(Role.ADMIN)).isEqualTo("/admin");
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void everyRoleHasALabel(Role role) {
        assertThat(LandingPages.labelFor(role)).isNotBlank();
    }

    @Test
    void readsRoleFromAuthorities() {
        Role role = LandingPages.roleOf(List.of(new SimpleGrantedAuthority("ROLE_INSPECTOR")));

        assertThat(role).isEqualTo(Role.INSPECTOR);
    }

    @Test
    void failsWhenUserHasNoRole() {
        assertThatThrownBy(() -> LandingPages.roleOf(List.of(new SimpleGrantedAuthority("SCOPE_read"))))
                .isInstanceOf(IllegalStateException.class);
    }
}
