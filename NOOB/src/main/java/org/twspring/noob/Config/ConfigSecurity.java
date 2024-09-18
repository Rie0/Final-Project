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
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
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
                        "/api/v1/pc-centre/add-pcCentre",
                        "/api/v1/pc-centre/delete-pcCentre/**",
                        "/api/v1/vendor/get-vendorbyid/**",   //getVendorById(Extra) :for vendor
                        "/api/v1/zone/chang-zone-status/**", // change zone status to Available :for vendor
                        "/api/v1/pc-centre/get-pcCentre-by-Vendor/**", //getPcCentersByVendorId(EXTRA):for vendor
                        "/api/v1/pc")
                .hasAuthority("VENDOR")

                .requestMatchers("/api/v1/PcCetreRating/add/**", //add review
                        "/api/v1/PcCetreRating/get-review-by-playerId", //getReviewCentreByPlayerId:for player
                        "/api/v1/SubscripeBy/player-return-subscription/**", // playerReturnSubscription: for player
                        "/api/v1/SubscripeBy/get-supscripe-by") //getSubscribeByforPlayer(EXTRA):for player)
                .hasAnyAuthority("PLAYER")

                .requestMatchers("/api/v1/bracket/delete/**",
                        "/api/v1/pc-centre/admin-Aproved-pcCenter/{PcCenterId}",  //approvePcCenter (BY ADMIN)
                        "/api/v1/vendor/get-all","/api/v1/pc-centre/get-all","/api/v1/pc-centre/get-not-approved-pc-centre").hasAuthority("ADMIN")

                .requestMatchers("/api/v1/admin/add", // add all users
                        "/api/v1/vendor/register", // register vendor
                        "/api/v1/player/register",// register player
                        "/api/v1/subscription/add-subscription/**", // add subscription
                        "/api/v1/SubscripeBy/add-SubscripeBy/**", //add subscribe BY
                        "/api/v1/zone/add-zone/**", //add zone
                        "/api/v1/pc-centre/ratings/**",  // getPcCentreByRatingRange:premit all & getPcCentreByRating
                        "/api/v1/pc-centre/get-pc-centre-by-name/**", //getPcCentreByName(EXTRA)permit all
                        "/api/v1/zone/get-zoneBy/**", //getZonesByPcCenter(EXTRA)permit all
                        "/api/v1/pc-centre/get-pcCentre/by/location/**", //getPcCentreByLocation(EXTRA)permit all
                        "/api/v1/SubscripeBy/supscripe/**", // playerSubscribe to pcCentre(EXTRA)permit all
                        "/api/v1/subscription/get-subscription/pc-center/**", //getSubscriptionsByPcCenter(EXTRA)permit all
                        "/api/v1/SubscripeBy/**",
                        "/api/v1/pc-centre/get-all-approved").permitAll()

                .and()
                .logout().logoutUrl("/api/v1/auth/logout").logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();

        return http.build();
    }
}
