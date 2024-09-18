package org.twspring.noob;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.twspring.noob.Controller.TournamentController;
import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.BracketService;
import org.twspring.noob.Service.TournamentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TournamentController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TournamentControllerTest {

    @MockBean
    TournamentService tournamentService;

    @MockBean
    BracketService bracketService;

    @Autowired
    MockMvc mockMvc;

    Tournament tournament1, tournament2;
    User user;
    Bracket bracket;
    Organizer organizer;

    List<Tournament> tournaments;

    @BeforeEach
    void setUp() {

        user = new User(1, "OrganizerUser", "ORGANIZER", "org@example.com", "password", "+966577345678", null, null, null, null, null, null, null);
        organizer = new Organizer(null, "Organizer1", "contact@example.com", "Organization1", null, null, user); // Set ID to null for generated ID

        tournament1 = new Tournament(null, "Tournament1", "Description1", LocalDate.now(), LocalDate.now().plusDays(1), Tournament.Status.OPEN, "Location1", 10, 0, "Chess", "Players", "City1", "Online",null, 1000, 500, null, null, null, organizer);
        tournament2 = new Tournament(null, "Tournament2", "Description2", LocalDate.now(), LocalDate.now().plusDays(2), Tournament.Status.ACTIVE, "Location2", 20, 5, "Soccer", "Teams", "City2", "Onsite",null, 2000, 1000, null, null, null, organizer);
        tournaments = Arrays.asList(tournament1, tournament2);
        bracket = new Bracket();
    }


    //passed Hussam
    @Test
    public void getAllTournamentsTest() throws Exception {
        Mockito.when(tournamentService.getTournaments()).thenReturn(tournaments);

        mockMvc.perform(get("/api/v1/tournament/get"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tournament1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Tournament2"));
    }



    //passed Hussam
    @Test
    public void getTournamentsByGameTest() throws Exception {
        Mockito.when(tournamentService.getTournamentsByGame("Chess")).thenReturn(tournaments);

        mockMvc.perform(get("/api/v1/tournament/by-game?game=Chess"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].game").value("Chess"));
    }


    //passed Hussam
    @Test
    public void getTournamentsByCityTest() throws Exception {
        Mockito.when(tournamentService.getTournamentsByCity("City1")).thenReturn(tournaments);

        mockMvc.perform(get("/api/v1/tournament/by-city?city=City1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city").value("City1"));
    }
}
