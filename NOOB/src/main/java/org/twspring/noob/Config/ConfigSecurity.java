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
                .requestMatchers("/api/v1/coach/register").permitAll()

               //ALL
                .requestMatchers(
                        //ADMIN (Always leave at comment)
                        "/api/v1/admin/add",
                        //player
                        "/api/v1/player/register",
                        "/api/v1/player/get/profile/{playerId}",
                        "/api/v1/player/get/profile/by-username/{username}",
                        //team
                        "/api/v1/team/register",
                        "/api/v1/team/get/profile/{teamId}",
                        //league
                        "/api/v1/league/get",
                        "/api/v1/league/get/{leagueId}",
                        "/api/v1/league/{leagueId}/get-all-participants",
                        "/api/v1/league/{leagueId}/get-rounds",
                        "/api/v1/league/{leagueId}/get-matches",
                        "/api/v1/league/{leagueId}/leaderboard",
                        //matches
                        "api/v1/match/get-by-participant/{participantId}",
                        "api/v1/match/get-by-id/{matchId}"
                        ).permitAll()
                .requestMatchers("/api/v1/organizer/add").permitAll() // Anyone can add an organizer
                .requestMatchers("/api/v1/tournament/get").permitAll() // Anyone can view tournaments
                .requestMatchers("/api/v1/match/get").permitAll() // Anyone can view matches
                .requestMatchers("/api/v1/participant/get").permitAll() // Anyone can view participants
                .requestMatchers("/api/v1/player/register").permitAll() // Anyone can Register player
                .requestMatchers("/api/v1/coach/register").permitAll() // Open registration for coaches
                .requestMatchers("/api/v1/coach/get-all").permitAll()
                .requestMatchers("/api/v1/coach/get/**").permitAll()
                .requestMatchers("/api/v1/tournament/get",
                        "/api/v1/tournament/by-game",
                        "/api/v1/tournament/by-city",
                        "/api/v1/tournament/online",
                        "/api/v1/tournament/onsite",
                        "/api/v1/tournament/status/ongoing",
                        "/api/v1/tournament/status/active",
                        "/api/v1/tournament/status/closing-soon",
                        "/api/v1/tournament/status/finished",
                        "/api/v1/tournament/{id}",
                        "/api/v1/tournament/{id}/description",
                        "/api/v1/tournament/{id}/matches",
                        "/api/v1/tournament/{id}/bracket",
                        "/api/v1/tournament/{id}/standing",
                        "/api/v1/tournament/{tournamentId}/matches/completed",
                        "/api/v1/tournament/{tournamentId}/matches/in-progress",
                        "/api/v1/tournament/{tournamentId}/matches/not-started").permitAll()

                //COACH
                .requestMatchers("/api/v1/coach/update/**", "/api/v1/coach/delete/**").hasAuthority("COACH")

                //ORGANIZER
                .requestMatchers("/api/v1/tournament/update/**",
                        "/api/v1/tournament/add",
                        "/api/v1/tournament/*/initializeBracket",
                        "/api/v1/tournament/*/initializeDoubleEliminationBracket",
                        "/api/v1/tournament/*/start",
                        "/api/v1/tournament/delete/**",
                        "/api/v1/tournament/*/finalize",
                        "api/v1/tournament/*/distribute-prizes",


                        //rounds
                        "/api/v1/round/add",
                        "/api/v1/round/update/**",
                        "/api/v1/round/delete/**",
                        //organizer
                        "/api/v1/organizer/update/**",
                        "/api/v1/organizer/delete/**",
                        //league
                        "/api/v1/league/create-new",
                        "/api/v1/league/{leagueId}/change-dates",
                        "/api/v1/league/{leagueId}/round/{roundId}/set-dates",
                        "/api/v1/league/{leagueId}/match/{matchId}/set-dates",
                        "/api/v1/league/{leagueId}/set-ready",
                        "/api/v1/league/{leagueId}/match/{matchId}/start-match",
                        "/api/v1/league/{leagueId}/match/{matchId}/finish-match",
                        "/api/v1/league/{leagueId}/match/{matchId}/add-1score-to-participant1",
                        "/api/v1/league/{leagueId}/match/{matchId}/add-1score-to-participant2",
                        "/api/v1/league/{leagueId}/match/{matchId}/sub-1score-from-participant1",
                        "/api/v1/league/{leagueId}/match/{matchId}/sub-1score-from-participant2",
                        "/api/v1/league/{leagueId}/finalize",
                        "/api/v1/league/delete/{id}",
                        "/api/v1/league/update/{id}",
                        "/api/v1/tournament/*/finalize",
                        "/api/v1/league/{leagueId}/match/{matchId}/cancel-match",
                        "/api/v1/league/{leagueId}/kick-participant/{playerId}",
                        "/api/v1/league/{leagueId}/withdraw"
                ).hasAuthority("ORGANIZER")

                .requestMatchers("/api/v1/tournament/*/participant/*/checkin").hasAnyAuthority("ADMIN", "ORGANIZER")

                //ADMIN AND ORGANIZER
                .requestMatchers(
                        "/api/v1/tournament/*/participant/*/checkin",
                        "/api/v1/bracket/get",
                        "api/v1/bracket/delete/{id}",
                        "/api/v1/bracket/add",
                        "/api/v1/bracket/update/**",
                        "/api/v1/round/get",
                        "/api/v1/match/add",
                        "/api/v1/match/update/**",
                        "/api/v1/match/delete/**",
                        "/api/v1/match/*/winner/bye"
                ).hasAnyAuthority("ADMIN", "ORGANIZER")

                //PLAYER AND ADMIN
                .requestMatchers("/api/v1/participant/add/**",
                        "/api/v1/participant/update/**",
                        "/api/v1/participant/get/**",
                        "/api/v1/participant/delete/**").hasAnyAuthority("PLAYER", "ADMIN")

                //ADMIN ORGANIZER PLAYER
                .requestMatchers(
                        "/api/v1/match/history-between-two-players",
                        "/api/v1/match/*/ready/**",
                        "/api/v1/match/*/setWinner",
                        "/api/v1/match/*/advanceWinner"
                ).hasAnyAuthority("ADMIN", "ORGANIZER", "PLAYER")

                //PLAYER
                .requestMatchers(
                        "/api/v1/player/update-my-info",
                        "/api/v1/player/edit-bio",
                        "/api/v1/player/delete-my-account",
                        "/api/v1/player/invites/get-invites",
                        "/api/v1/player/invites/{inviteId}/accept",
                        "/api/v1/player/invites/{inviteId}/decline",
                        "/api/v1/player/team/leave",
                        //league
                        "/api/v1/league/{leagueId}/participate",
                        "/api/v1/league/{leagueId}/withdraw",
                        //match
                        "/api/v1/match/{matchId}/ready/participant1",
                        "/api/v1/match/{matchId}/ready/participant2"
                ).hasAuthority("PLAYER")

                //ADMIN
                .requestMatchers(
                        "/api/v1/bracket/delete/**",

                        "/api/v1/player/get-all",
                        "/api/v1/player/get/{playerId}",
                        "/api/v1/player/delete-account-by-admin/{id}",

                        "/api/v1/team/get-all",
                        "/api/v1/team/get/{teamId}",
                        "/api/v1/organizer/get",
                        "/api/v1/league/admin/delete/{id}",
                        "api/v1/league/admin/update/{id}"
                ).hasAuthority("ADMIN")

                //TEAM
                .requestMatchers(
                        "/api/v1/team/update-my-info",
                        "/api/v1/team/delete-my-account",
                        "/api/v1/team/players/get-all",
                        "/api/v1/team/invites/get-invites",
                        "/api/v1/team/invites/invite/{PlayerUsername}",
                        "/api/v1/team/invites/invite-multiple-players",
                        "/api/v1/team/invites/{inviteId}/delete",
                        "/api/v1/team/update-bio"
                ).hasAuthority("TEAM")

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
//                .requestMatchers(
//                        "/api/v1/player/**"
//                ).hasAuthority("ADMIN")

                .and()
                .logout().logoutUrl("/api/v1/auth/logout").logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and();

        return http.build();
    }
}

