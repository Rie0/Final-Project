package org.twspring.noob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.OrganizerRepository;
import org.twspring.noob.Repository.TournamentRepository;
import org.twspring.noob.Service.TournamentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @InjectMocks
    TournamentService tournamentService;

    @Mock
    TournamentRepository tournamentRepository;

    @Mock
    OrganizerRepository organizerRepository;

    User user;
    Organizer organizer;
    Tournament tournament1, tournament2;
    List<Tournament> tournaments;

    @BeforeEach
    void setUp() {

        user = new User(null, "johnDoe", "ORGANIZER", "john@example.com", "password", "+966512345678", null, null,null,null, null, null, null);
        organizer = new Organizer(1, "Organizer1", "contact@example.com", "Organization1",null,null,user);
        tournament1 = new Tournament(1, "Tournament1", "Description1", LocalDate.now(), LocalDate.now().plusDays(1), Tournament.Status.ACTIVE, "Location1", 10, 5, "Chess", "Teams", "City1", "Online",null, 1000, 200, null, null, null, organizer);
        tournament2 = new Tournament(2, "Tournament2", "Description2", LocalDate.now(), LocalDate.now().plusDays(1), Tournament.Status.FINISHED, "Location2", 20, 10, "Soccer", "Players", "City2", "Online",null, 5000, 500, null, null, null, organizer);


        tournaments = new ArrayList<>();
        tournaments.add(tournament1);
        tournaments.add(tournament2);
    }


    //passed
    @Test
    public void getAllTournamentsTest() {
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        List<Tournament> tournamentList = tournamentService.getTournaments();
        assertEquals(2, tournamentList.size());
        verify(tournamentRepository, times(1)).findAll();
    }


    //passed Hussam

    @Test
    public void getTournamentByIdTest() {
        when(tournamentRepository.findTournamentById(tournament1.getId())).thenReturn(tournament1);
        Tournament foundTournament = tournamentService.getTournamentById(tournament1.getId());
        assertEquals(tournament1, foundTournament);
        verify(tournamentRepository, times(1)).findTournamentById(tournament1.getId());
    }


    //passed Hussam
    @Test
    public void saveTournamentTest() {
        when(organizerRepository.findOrganizerById(organizer.getId())).thenReturn(organizer);
        tournamentService.saveTournament(tournament1, organizer.getId());
        verify(organizerRepository, times(1)).findOrganizerById(organizer.getId());
        verify(tournamentRepository, times(1)).save(tournament1);
    }

    //Passed Hussam
    @Test
    public void deleteTournamentUnauthorizedTest() {
        when(tournamentRepository.findByTournamentIdAndOrganizerId(tournament1.getId(), organizer.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> tournamentService.deleteTournament(tournament1.getId(), organizer.getId()));
        verify(tournamentRepository, times(1)).findByTournamentIdAndOrganizerId(tournament1.getId(), organizer.getId());
    }





}
