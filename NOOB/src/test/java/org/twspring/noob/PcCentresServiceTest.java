package org.twspring.noob;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.VendorRepository;
import org.twspring.noob.Service.PcCentresService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PcCentresServiceTest {

    @Mock
    private PcCentresRepository pcCentresRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private PcCentresService pcCentresService;

    private PcCentres pcCentre;
    private Vendor vendor;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Initialize test objects
        vendor = new Vendor();
        vendor.setId(1);
        vendor.setVenueName("Vendor 1");

        pcCentre = new PcCentres();
        pcCentre.setId(1);
        pcCentre.setCentreName("Tech Centre 1");
        pcCentre.setLocation("New York");
        pcCentre.setManyReview(5);
        pcCentre.setVendor(vendor);
        pcCentre.setApproved(false);

        vendor.setPcCentres(Set.of(pcCentre));

        adminUser = new User();
        adminUser.setId(1);
        adminUser.setRole("ADMIN");
    }

    @Test
    void testAddPcCentres_ValidVendor() {
        when(vendorRepository.findVendorById(vendor.getId())).thenReturn(vendor);

        pcCentresService.addPcCentres(pcCentre, vendor.getId());

        verify(pcCentresRepository).save(pcCentre);
    }

    @Test
    void testAddPcCentres_InvalidVendor() {
        when(vendorRepository.findVendorById(anyInt())).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () ->
                pcCentresService.addPcCentres(pcCentre, 999));

        assertEquals("Vendor with ID 999 not found", exception.getMessage());
        verify(pcCentresRepository, never()).save(any(PcCentres.class));
    }

    @Test
    void testUpdatePcCentres_ValidId() {
        when(pcCentresRepository.findPcCentreById(pcCentre.getId())).thenReturn(pcCentre);

        PcCentres updatedPcCentre = new PcCentres();
        updatedPcCentre.setCentreName("Updated Centre");
        updatedPcCentre.setLocation("Los Angeles");
        updatedPcCentre.setNumberOfPc(30);
        updatedPcCentre.setManyReview(4);

        pcCentresService.updatePcCentres(pcCentre.getId(), updatedPcCentre);

        assertEquals("Updated Centre", pcCentre.getCentreName());
        assertEquals("Los Angeles", pcCentre.getLocation());
        assertEquals(30, pcCentre.getNumberOfPc());
        assertEquals(4, pcCentre.getManyReview());
        verify(pcCentresRepository).save(pcCentre);
    }

    @Test
    void testUpdatePcCentres_InvalidId() {
        when(pcCentresRepository.findPcCentreById(anyInt())).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () ->
                pcCentresService.updatePcCentres(999, pcCentre));

        assertEquals("PC Centre not found", exception.getMessage());
        verify(pcCentresRepository, never()).save(any(PcCentres.class));
    }

    @Test
    void testGetPcCentresByVendorID_ValidVendor() {
        when(vendorRepository.findVendorById(vendor.getId())).thenReturn(vendor);
        when(pcCentresRepository.getPcCentresByVendorId(vendor.getId()))
                .thenReturn(Arrays.asList(pcCentre));

        List<PcCentres> result = pcCentresService.getPcCentresByVendorID(vendor.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pcCentre.getCentreName(), result.get(0).getCentreName());
    }

    @Test
    void testGetPcCentresByVendorID_InvalidVendor() {
        when(vendorRepository.findVendorById(anyInt())).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () ->
                pcCentresService.getPcCentresByVendorID(999));

        assertEquals("Vendor with ID 999 not found", exception.getMessage());
        verify(pcCentresRepository, never()).getPcCentresByVendorId(anyInt());
    }

    @Test
    void testAdminAprovedPcCenter_ValidAdminAndPcCentre() {
        when(authRepository.findUserById(adminUser.getId())).thenReturn(adminUser);
        when(pcCentresRepository.findPcCentreById(pcCentre.getId())).thenReturn(pcCentre);

        pcCentresService.adminAprovedPcCenter(adminUser.getId(), pcCentre.getId());

        assertTrue(pcCentre.isApproved());
        verify(pcCentresRepository).save(pcCentre);
    }

    @Test
    void testAdminAprovedPcCenter_InvalidAdmin() {
        when(authRepository.findUserById(anyInt())).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () ->
                pcCentresService.adminAprovedPcCenter(999, pcCentre.getId()));

        assertEquals("User not found", exception.getMessage());
        verify(pcCentresRepository, never()).save(any(PcCentres.class));
    }

    @Test
    void testGetPcCentresByRating_ValidRating() {
        when(pcCentresRepository.findPcCentresByRating(5)).thenReturn(Arrays.asList(pcCentre));

        List<PcCentres> result = pcCentresService.getPcCentresByRating(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getManyReview());
    }

    @Test
    void testGetPcCentresByRatingRange() {
        when(pcCentresRepository.findByRatingBetween(4, 5))
                .thenReturn(Arrays.asList(pcCentre));

        List<PcCentres> result = pcCentresService.getPcCentresByRatingRange(4, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}

