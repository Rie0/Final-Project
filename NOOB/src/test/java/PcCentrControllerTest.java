import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.twspring.noob.Controller.PcCentresController;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.PcCentresService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;



////Hassan Alzahrani
class PcCentresControllerTest {

    @Mock
    private PcCentresService pcCentresService;

    @InjectMocks
    private PcCentresController pcCentresController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPcCentres_ShouldReturnListOfPcCentres() {
        // Arrange
        List<PcCentres> pcCentresList = new ArrayList<>();
        pcCentresList.add(new PcCentres());
        when(pcCentresService.getAllPcCentres()).thenReturn(pcCentresList);

        // Act
        ResponseEntity response = pcCentresController.getAllPcCentres();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pcCentresList, response.getBody());
    }

    @Test
    void addPcCentres_ShouldReturnSuccessMessage() {
        // Arrange
        PcCentres pcCentre = new PcCentres();
        doNothing().when(pcCentresService).addPcCentres(any(PcCentres.class), anyInt());

        // Act
        ResponseEntity response = pcCentresController.addPcCentres(1, pcCentre);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PC Centre added successfully", response.getBody());
    }

    @Test
    void updatePcCentres_ShouldReturnSuccessMessage() {
        // Arrange
        PcCentres pcCentre = new PcCentres();
        doNothing().when(pcCentresService).updatePcCentres(anyInt(), any(PcCentres.class));

        // Act
        ResponseEntity response = pcCentresController.updatePcCentres(1, pcCentre);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PC Centres  updated successfully", response.getBody());
    }

    @Test
    void deletePcCentres_ShouldReturnSuccessMessage() {
        // Arrange
        doNothing().when(pcCentresService).deletePcCentres(anyInt());

        // Act
        ResponseEntity response = pcCentresController.deletePcCentres(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PC Centres deleted successfully", response.getBody());
    }

    @Test
    void getPcCentreByVendor_ShouldReturnPcCentresList() {
        // Arrange
        List<PcCentres> pcCentresList = new ArrayList<>();
        when(pcCentresService.getPcCentresByVendorID(anyInt())).thenReturn(pcCentresList);

        // Act
        ResponseEntity response = pcCentresController.getPcCentreByVendor(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pcCentresList, response.getBody());
    }

    @Test
    void approvedPcCenter_ShouldReturnSuccessMessage() {
        // Arrange
        doNothing().when(pcCentresService).adminAprovedPcCenter(anyInt());

        // Act
        ResponseEntity response = pcCentresController.approvedPcCenter( 1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("approved PC Center successfully", response.getBody());
    }

    @Test
    void getRatings_ShouldReturnPcCentresList() {
        // Arrange
        List<PcCentres> pcCentresList = new ArrayList<>();
        when(pcCentresService.getPcCentresByRating(anyInt())).thenReturn(pcCentresList);

        // Act
        ResponseEntity response = pcCentresController.getRatings(5);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pcCentresList, response.getBody());
    }

    @Test
    void getPcCentresByRatingRange_ShouldReturnPcCentresList() {
        // Arrange
        List<PcCentres> pcCentresList = new ArrayList<>();
        when(pcCentresService.getPcCentresByRatingRange(anyInt(), anyInt())).thenReturn(pcCentresList);

        // Act
        List<PcCentres> result = pcCentresController.getPcCentresByRatingRange(1, 5);

        // Assert
        assertEquals(pcCentresList, result);
    }

    @Test
    void getPcCentreByName_ShouldReturnPcCentres() {
        // Arrange
        PcCentres pcCentre = new PcCentres();
        when(pcCentresService.getPcCentreByCentreName(any())).thenReturn(pcCentre);

        // Act
        ResponseEntity response = pcCentresController.getPcCentreByName("TestName");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pcCentre, response.getBody());
    }

    @Test
    void getPcCentreByLocation_ShouldReturnPcCentresList() {
        // Arrange
        List<PcCentres> pcCentresList = new ArrayList<>();
        when(pcCentresService.getPcCentreByLocation(any())).thenReturn(pcCentresList);

        // Act
        ResponseEntity response = pcCentresController.getPcCentreByLocation("TestLocation");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pcCentresList, response.getBody());
    }
}