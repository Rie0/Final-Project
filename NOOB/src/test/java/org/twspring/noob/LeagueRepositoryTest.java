package org.twspring.noob;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twspring.noob.Model.League;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.LeagueRepository;
import org.twspring.noob.Repository.OrganizerRepository;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LeagueRepositoryTest {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    OrganizerRepository organizerRepository;

    @Autowired
    AuthRepository userRepository;

    User user ;
    Organizer organizer;
    League league1, league2, league3;

    @BeforeEach
    void setUp() {

        user = new User(1, "Organizer1", "OrGANIZER!1", "ORGANIZER", "ORG@gmail.com", "+966577345678", LocalDate.parse("2000-11-01") , 20, null, null, null, null, null);
        userRepository.save(user);
        organizer = new Organizer(1, "Organizer1", "contact@example.com", "Organization1", null, null, user);
        organizerRepository.save(organizer);

        league1 = new League (1, "League1", "Description1", LocalDate.now(), LocalDate.now().plusDays(1), League.Status.ONGOING, "online",  10, 0, organizer,null,null,null);
        league2 = new League (2, "League2", "Description2", LocalDate.now(), LocalDate.now().plusDays(1), League.Status.ONGOING, "online",  10, 0, organizer,null,null,null);
    }

    @Test
    public void findLeagueByIdTest() {
        leagueRepository.save(league1);
        League foundLeague = leagueRepository.findLeagueById(league1.getId());
        Assertions.assertThat(foundLeague).isNotNull();
        Assertions.assertThat(foundLeague.getId()).isEqualTo(league1.getId());
    }

    @Test
    public void findAllLeaguesTest() {
        leagueRepository.save(league1);
        leagueRepository.save(league2);

        Iterable<League> allLeagues = leagueRepository.findAll();

        Assertions.assertThat(allLeagues).hasSize(2);
    }
}
