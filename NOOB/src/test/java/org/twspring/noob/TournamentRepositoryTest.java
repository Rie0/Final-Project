package org.twspring.noob;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.OrganizerRepository;
import org.twspring.noob.Repository.TournamentRepository;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TournamentRepositoryTest {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    OrganizerRepository organizerRepository;

    @Autowired
    AuthRepository userRepository;


    User user ;
    Organizer organizer;
    Tournament tournament1, tournament2, tournament3;

    @BeforeEach
    void setUp() {

        // Create a new User entity and save it to the repository
        user = new User(1, "Organizer1", "OrGANIZER!1", "ORGANIZER", "ORG@gmail.com", "+966577345678",LocalDate.parse("2000-11-01") , 20, null, null, null, null, null);
        userRepository.save(user);

        // Create the Organizer entity with the user entity already persisted
        organizer = new Organizer(1, "Organizer1", "contact@example.com", "Organization1", null, null, user); // Set ID to null for generated ID
        organizerRepository.save(organizer); // Save the Organizer entity after the User has been saved

        // Create Tournament entities linked to the Organizer
        tournament1 = new Tournament(null, "Tournament1", "Description1", LocalDate.now(), LocalDate.now().plusDays(1), Tournament.Status.OPEN, "Location1", 10, 0, "Chess", "Players", "City1", "Online",null, 1000, 500, null, null, null, organizer);
        tournament2 = new Tournament(null, "Tournament2", "Description2", LocalDate.now(), LocalDate.now().plusDays(2), Tournament.Status.ACTIVE, "Location2", 20, 5, "Soccer", "Teams", "City2", "Online",null, 2000, 1000, null, null, null, organizer);
        tournament3 = new Tournament(null, "Tournament3", "Description3", LocalDate.now().plusDays(3), LocalDate.now().plusDays(4), Tournament.Status.FINISHED, "Location3", 30, 10, "Basketball", "Teams", "City3","Online", null, 3000, 1500, null, null, null, organizer);
    }


    //Passed Hussam
    @Test
    public void findTournamentByIdTest() {
        tournamentRepository.save(tournament1);
        Tournament foundTournament = tournamentRepository.findTournamentById(tournament1.getId());
        Assertions.assertThat(foundTournament).isNotNull();
        Assertions.assertThat(foundTournament.getId()).isEqualTo(tournament1.getId());
    }


    //Passed Hussam

    @Test
    public void findByGameTest() {
        tournamentRepository.save(tournament1);
        tournamentRepository.save(tournament2);
        tournamentRepository.save(tournament3);

        List<Tournament> chessTournaments = tournamentRepository.findByGame("Chess");
        Assertions.assertThat(chessTournaments.size()).isEqualTo(1);
        Assertions.assertThat(chessTournaments.get(0).getGame()).isEqualTo("Chess");
    }

    //Passed Hussam
    @Test
    public void findByCityTest() {
        tournamentRepository.save(tournament1);
        tournamentRepository.save(tournament2);
        tournamentRepository.save(tournament3);

        List<Tournament> city2Tournaments = tournamentRepository.findByCity("City2");
        Assertions.assertThat(city2Tournaments.size()).isEqualTo(1);
        Assertions.assertThat(city2Tournaments.get(0).getCity()).isEqualTo("City2");
    }

    //Passed Hussam
    @Test
    public void findByAttendanceTypeTest() {
        tournamentRepository.save(tournament1);
        tournamentRepository.save(tournament2);
        tournamentRepository.save(tournament3);

        List<Tournament> onlineTournaments = tournamentRepository.findByAttendanceType("Online");
        Assertions.assertThat(onlineTournaments.size()).isEqualTo(2);
    }
    //Passed Hussam
    @Test
    public void findByTournamentIdAndOrganizerIdTest() {
        tournamentRepository.save(tournament1);
        Tournament foundTournament = tournamentRepository.findByTournamentIdAndOrganizerId(tournament1.getId(), organizer.getId());
        Assertions.assertThat(foundTournament).isNotNull();
        Assertions.assertThat(foundTournament.getId()).isEqualTo(tournament1.getId());
        Assertions.assertThat(foundTournament.getOrganizer().getId()).isEqualTo(organizer.getId());
    }

    //Passed Hussam

    @Test
    public void findUpcomingTournamentsTest() {
        tournamentRepository.save(tournament1);
        tournamentRepository.save(tournament2);
        tournamentRepository.save(tournament3);

        List<Tournament> upcomingTournaments = tournamentRepository.findUpcomingTournaments();
        Assertions.assertThat(upcomingTournaments.size()).isGreaterThanOrEqualTo(2);
        Assertions.assertThat(upcomingTournaments.get(0).getStartDate()).isAfterOrEqualTo(LocalDate.now());
    }
}
