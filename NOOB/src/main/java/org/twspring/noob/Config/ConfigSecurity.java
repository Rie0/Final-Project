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
                .and() //Authorization
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()
                .requestMatchers(

                        //player
                        "/api/v1/player/get-all",
                        "/api/v1/player/register",
                        "/api/v1/player/get/{playerId}"
                        ).permitAll()
                //COACH
                //.requestMatchers().hasAuthority("COACH")
                //ORGANIZER
                //.requestMatchers().hasAuthority("ORGANIZER")
                //PLAYER
                .requestMatchers(
                        "/api/v1/player/{playerId}/update-my-info",
                        "/api/v1/player/{playerId}/edit-bio",
                        "/api/v1/player/{playerId}/delete-my-account",
                        "/api/v1/player/{playerId}/invites/get-invites",
                        "/api/v1/player/{playerId}/invites/{inviteId}/accept",
                        "/api/v1/player/{playerId}/invites/{inviteId}/decline",
                        "/api/v1/player/{playerId}/team/leave"
                ).hasAuthority("PLAYER")
                //ADMIN
                //.requestMatchers().hasAuthority("ADMIN")
                //TEAM
                //.requestMatchers().hasAuthority("TEAM")
                .and()
                .logout().logoutUrl("/api/v1/auth/logout").logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();

        return http.build();
    }
}
