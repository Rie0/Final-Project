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
                // Allow open access for specific endpoints
                .requestMatchers("/api/v1/organizer/add").permitAll() // Anyone can add an organizer
                .requestMatchers("/api/v1/tournament/get").permitAll() // Anyone can view tournaments
                .requestMatchers("/api/v1/match/get").permitAll() // Anyone can view matches
                .requestMatchers("/api/v1/participant/get").permitAll() // Anyone can view participants
                .requestMatchers("/api/v1/player/register").permitAll() // Anyone can Register player
                .requestMatchers("/api/v1/coach/register").permitAll() // Open registration for coaches
                .requestMatchers("/api/v1/coach/get-all").permitAll()
                .requestMatchers("/api/v1/coach/get/**").permitAll()

                // Admin and Coach can view all coaches
                .requestMatchers("/api/v1/coach/update/**", "/api/v1/coach/delete/**").hasAuthority("COACH") // Only the coach can update or delete their info
                // Admin and Coach can get coach details
                // Specific roles required for other endpoints
                .requestMatchers("/api/v1/tournament/update/**",
                        "/api/v1/tournament/*/initializeBracket",
                        "/api/v1/tournament/*/start",
                        "/api/v1/tournament/delete/**",
                        "/api/v1/tournament/*/finalize").hasAuthority("ORGANIZER")
                .requestMatchers("/api/v1/tournament/*/participant/*/checkin").hasAnyAuthority("ADMIN", "ORGANIZER")

                .requestMatchers("/api/v1/participant/add/**",
                        "/api/v1/participant/update/**",
                        ("/api/v1/participant/get/**"),
                        "/api/v1/participant/delete/**").hasAnyAuthority("PLAYER", "ADMIN")

                .requestMatchers("/api/v1/bracket/get",
                        "/api/v1/bracket/add",
                        "/api/v1/bracket/update/**").hasAnyAuthority("ADMIN", "ORGANIZER")
                .requestMatchers("/api/v1/bracket/delete/**").hasAuthority("ADMIN")

                .requestMatchers("/api/v1/round/add",
                        "/api/v1/round/update/**",
                        "/api/v1/round/delete/**").hasAuthority("ORGANIZER")
                .requestMatchers("/api/v1/round/get").hasAnyAuthority("ADMIN", "ORGANIZER")

                .requestMatchers("/api/v1/organizer/update/**",
                        "/api/v1/organizer/delete/**").hasAuthority("ORGANIZER")

                .requestMatchers("/api/v1/match/history-between-two-players").hasAnyAuthority("ADMIN", "ORGANIZER", "PLAYER")
                .requestMatchers("/api/v1/match/add",
                        "/api/v1/match/update/**",
                        "/api/v1/match/delete/**",
                        "/api/v1/match/*/winner/bye").hasAnyAuthority("ADMIN", "ORGANIZER")
                .requestMatchers("/api/v1/match/*/ready/**",
                        "/api/v1/match/*/setWinner",
                        "/api/v1/match/*/advanceWinner").hasAnyAuthority("ADMIN", "ORGANIZER", "PLAYER")

                // All other endpoints require authentication
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/api/v1/auth/logout").logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();

        return http.build();
    }
}
