package org.twspring.noob;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.VendorRepository;

import java.util.List;
import java.util.Set;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PcCentresRepositoryTest {
    @Autowired
    private PcCentresRepository pcCentresRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private AuthRepository authRepository;

    private PcCentres pcCentre1;
    private PcCentres pcCentre2;
    User user;

    @BeforeEach
    void setUp() {

        user = new User(null, "Dummy", "Dummy123$", "PLAYER", "hhahahah@gmail.com", "+966512345677", null, null, null);

        pcCentre1 = new PcCentres();
        pcCentre1.setCentreName("Tech Centre 1");
        pcCentre1.setLocation("New York");
        pcCentre1.setNumberOfPc(20);
        pcCentre1.setManyReview(5);
        pcCentre1.setOpeningHours("5");
        pcCentre1.setContactNumber("+966512345678");
        pcCentre1.setCommercialregister("abx");
        pcCentre1.setApproved(false);


        pcCentre2 = new PcCentres();
        pcCentre2.setCentreName("Tech Centre 2");
        pcCentre2.setLocation("Los Angeles");
        pcCentre2.setNumberOfPc(15);
        pcCentre2.setManyReview(4);
        pcCentre2.setOpeningHours("5");
        pcCentre2.setContactNumber("+966512345679");
        pcCentre2.setCommercialregister("abx");
        pcCentre2.setApproved(false);


        authRepository.save(user);

        Vendor vendor1 =new Vendor(null, "venue1", user, Set.of(pcCentre1));
        vendorRepository.save(vendor1);

        pcCentre1.setVendor(vendor1);
        pcCentre2.setVendor(vendor1);

        pcCentresRepository.save(pcCentre1);
        pcCentresRepository.save(pcCentre2);
    }

    @Test
    void testFindPcCentreById() {
        PcCentres foundPcCentre = pcCentresRepository.findPcCentreById(pcCentre1.getId());
        Assertions.assertNotNull(foundPcCentre);
        Assertions.assertEquals("Tech Centre 1", foundPcCentre.getCentreName());
    }

    @Test
    void testGetPcCentresByVendorId() {
        List<PcCentres> pcCentresList = pcCentresRepository.getPcCentresByVendorId(pcCentre1.getVendor().getId());
        Assertions.assertNotNull(pcCentresList);
        Assertions.assertTrue(pcCentresList.size() > 0);
    }

    @Test
    void testFindPcCentresByRating() {
        List<PcCentres> pcCentresList = pcCentresRepository.findPcCentresByRating(5);
        Assertions.assertNotNull(pcCentresList);
        Assertions.assertEquals(1, pcCentresList.size());
    }

    @Test
    void testFindByRatingBetween() {
        List<PcCentres> pcCentresList = pcCentresRepository.findByRatingBetween(4, 5);
        Assertions.assertNotNull(pcCentresList);
        Assertions.assertEquals(2, pcCentresList.size());
    }

    @Test
    void testFindPcCentresByCentreName() {
        PcCentres foundPcCentre = pcCentresRepository.findPcCentresByCentreName("Tech Centre 1");
        Assertions.assertNotNull(foundPcCentre);
        Assertions.assertEquals(pcCentre1.getCentreName(), foundPcCentre.getCentreName());
    }

    @Test
    void testFindPcCentresByLocation() {
        List<PcCentres> pcCentresList = pcCentresRepository.findPcCentresByLocation("New York");
        Assertions.assertNotNull(pcCentresList);
        Assertions.assertEquals(1, pcCentresList.size());
        Assertions.assertEquals("New York", pcCentresList.get(0).getLocation());
    }
}