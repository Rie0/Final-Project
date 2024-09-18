package org.twspring.noob;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Subscription;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Zone;
import org.twspring.noob.Repository.SubscriptionRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


////Hassan Alzahrani
@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class SubscriptionRepositoryTest {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private Subscription validSubscription;
    User user;

    @BeforeEach
    void setUp() {
        // Create a valid Subscription object
        validSubscription = new Subscription();
        validSubscription.setSubscriptionNmae("Premium Plan");
        validSubscription.setPrice(99.99);
        validSubscription.setSubscriptionHours(100);
        validSubscription.setMembers(10);
        validSubscription.setSubscriptionDate(new Date());
        validSubscription.setCoupon("DISCOUNT10");

        PcCentres pcCentre1 = new PcCentres();
        pcCentre1.setCentreName("Tech Centre 1");
        pcCentre1.setLocation("New York");
        pcCentre1.setNumberOfPc(20);
        pcCentre1.setManyReview(5);
        pcCentre1.setOpeningHours("5");
        pcCentre1.setContactNumber("+966512345671");
        pcCentre1.setCommercialregister("abx");
        pcCentre1.setApproved(false);

        Zone zone = new Zone();
        zone.setZoneType("Zone A");

        Set<Zone> zones = new HashSet<>();
        zones.add(zone);

        validSubscription.setZones(zones);
        validSubscription.setPcCentres(pcCentre1);
    }

    @Test
    void saveValidSubscription_ShouldSaveSuccessfully() {
        Subscription savedSubscription = subscriptionRepository.save(validSubscription);

        assertNotNull(savedSubscription.getId());
        assertEquals("Premium Plan", savedSubscription.getSubscriptionNmae());
        assertEquals(99.99, savedSubscription.getPrice());
        assertEquals(100, savedSubscription.getSubscriptionHours());
        assertEquals(10, savedSubscription.getMembers());
    }

    @Test
    void findById_ShouldReturnSubscription() {
        Subscription savedSubscription = subscriptionRepository.save(validSubscription);

        Optional<Subscription> retrievedSubscription = subscriptionRepository.findById(savedSubscription.getId());

        assertTrue(retrievedSubscription.isPresent());
        assertEquals(savedSubscription.getId(), retrievedSubscription.get().getId());
    }

    @Test
    void deleteSubscription_ShouldRemoveSuccessfully() {
        Subscription savedSubscription = subscriptionRepository.save(validSubscription);

        subscriptionRepository.deleteById(savedSubscription.getId());

        Optional<Subscription> deletedSubscription = subscriptionRepository.findById(savedSubscription.getId());
        assertTrue(deletedSubscription.isEmpty());
    }

    @Test
    void updateSubscription_ShouldUpdateFieldsSuccessfully() {
        Subscription savedSubscription = subscriptionRepository.save(validSubscription);

        savedSubscription.setSubscriptionNmae("Updated Plan");
        savedSubscription.setPrice(199.99);

        Subscription updatedSubscription = subscriptionRepository.save(savedSubscription);

        assertEquals("Updated Plan", updatedSubscription.getSubscriptionNmae());
        assertEquals(199.99, updatedSubscription.getPrice());
    }
}