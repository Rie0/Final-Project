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
import org.twspring.noob.Controller.LeagueController;
import org.twspring.noob.Model.*;
import org.twspring.noob.Service.LeagueService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LeagueController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class LeagueControllerTest {

    @MockBean
    LeagueService leagueService;

    @Autowired
    MockMvc mockMvc;

    League league1,league2;
    User user;
    Organizer organizer;
    List<League> leagues;

    @BeforeEach
    void setUp() {

        user = new User(1, "OrganizerUser", "ORGANIZER", "org@example.com", "password", "+966577345678", null, null, null, null, null, null, null);
        organizer = new Organizer(1, "Organizer1", "contact@example.com", "Organization1", null, null, user); // Set ID to null for generated ID

        league1 = new League (1, "League1", "Description1", LocalDate.now(), LocalDate.now().plusDays(1), League.Status.ONGOING, "online",  10, 0, organizer,null,null,null);
        league2 = new League (2, "League2", "Description2", LocalDate.now(), LocalDate.now().plusDays(1), League.Status.ONGOING, "online",  10, 0, organizer,null,null,null);

        leagues = Arrays.asList(league1, league2);
    }

    @Test
    public void getAllLeaguesTest() throws Exception {
        Mockito.when(leagueService.getLeagues()).thenReturn(leagues);

        mockMvc.perform(get("/api/v1/league/get"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("League1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("League2"));
    }

    @Test
    public void getLeagueByIdTest() throws Exception {
        Mockito.when(leagueService.getLeagueById(league1.getId())).thenReturn(league1);

        mockMvc.perform(get("/api/v1/league/get/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("League1"));
    }
}
