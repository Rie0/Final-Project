package org.twspring.noob.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.twspring.noob.Service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {
    private final MyUserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()

                // Coach
                .requestMatchers(
                        "/api/v1/coach/get-all",
                        "/api/v1/coach/register",
                        "/api/v1/coach/get/{coachId}",
                        "/api/v1/coach/get-by-hourly-rate-range/{minRate}/{maxRate}",
                        "/api/v1/coach/reviews/{coachId}"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/coach/update",
                        "/api/v1/coach/delete"
                ).hasAnyAuthority("COACH", "ADMIN")
                .requestMatchers(
                        "/api/v1/coach/get-all",
                        "/api/v1/coach/register",
                        "/api/v1/coach/update",
                        "/api/v1/coach/delete",
                        "/api/v1/coach/get/{coachId}",
                        "/api/v1/coach/get-by-hourly-rate-range/{minRate}/{maxRate}",
                        "/api/v1/coach/reviews/{coachId}"
                ).hasAuthority("ADMIN")

                // Coaching Session
                .requestMatchers(
                        "/api/v1/coaching-session/get-all",
                        "/api/v1/coaching-session/get/{sessionId}"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/coaching-session/reserve/{scheduleId}",
                        "/api/v1/coaching-session/request-reschedule/{coachingSessionId}"
                ).hasAnyAuthority("PLAYER", "ADMIN")
                .requestMatchers(
                        "/api/v1/coaching-session/update/{sessionId}",
                        "/api/v1/coaching-session/delete/{sessionId}",
                        "/api/v1/coaching-session/pending-approval-sessions",
                        "/api/v1/coaching-session/approve-reschedule/{coachingSessionId}",
                        "/api/v1/coaching-session/reject-reschedule/{coachingSessionId}/{coachId}",
                        "/api/v1/coaching-session/end/{coachingSessionId}"
                ).hasAnyAuthority("COACH", "ADMIN")
                .requestMatchers(
                        "/api/v1/coaching-session/**"
                ).hasAuthority("ADMIN")

                // Master Class
                .requestMatchers(
                        "/api/v1/masterclass/get-all",
                        "/api/v1/masterclass/register",
                        "/api/v1/masterclass/get/{id}",
                        "/api/v1/masterclass/get-by-coach/{coachId}",
                        "/api/v1/masterclass/by-coach-and-status/{coachId}/{status}"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/masterclass/update/{masterClassId}",
                        "/api/v1/masterclass/delete/{masterClassId}",
                        "/api/v1/masterclass/start/{masterClassId}",
                        "/api/v1/masterclass/close/{masterClassId}",
                        "/api/v1/masterclass/cancel/{masterClassId}",
                        "/api/v1/masterclass/kick/{masterClassId}/{playerId}"
                ).hasAnyAuthority("COACH", "ADMIN")
                .requestMatchers(
                        "/api/v1/masterclass/join/{masterClassId}",
                        "/api/v1/masterclass/leave/{masterClassId}"
                ).hasAnyAuthority("PLAYER", "ADMIN")
                .requestMatchers(
                        "/api/v1/masterclass/**"
                ).hasAuthority("ADMIN")

                // Schedule
                .requestMatchers(
                        "/api/v1/schedule/get-all",
                        "/api/v1/schedule/get/{scheduleId}",
                        "/api/v1/schedule/get-by-coach/{coachId}"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/schedule/register",
                        "/api/v1/schedule/update/{scheduleId}",
                        "/api/v1/schedule/delete/{scheduleId}"
                ).hasAnyAuthority("COACH", "ADMIN")
                .requestMatchers(
                        "/api/v1/schedule/**"
                ).hasAuthority("ADMIN")

                // Player
                .requestMatchers(
                        "/api/v1/player/get-all-reviews",
                        "/api/v1/player/get/{reviewId}"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/player/add-review/{coachId}/{coachingSessionId}/{comment}/{rating}",
                        "/api/v1/player/update-review/{reviewId}",
                        "/api/v1/player/delete-review/{reviewId}",
                        "/api/v1/player/get-by-player/{playerId}"
                ).hasAnyAuthority("PLAYER", "ADMIN")
                .requestMatchers(
                        "/api/v1/player/**"
                ).hasAuthority("ADMIN")

                .and()
                .logout().logoutUrl("/api/v1/auth/logout").logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and();

        return http.build();
    }
}

