package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Repository.PcCentresRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PcCentresService {

    private final PcCentresRepository pcCentresRepository;

    public List<PcCentres> getAllPcCentres() {
        return pcCentresRepository.findAll();
    }


    public void addPcCentres(PcCentres pcCentres) {
        pcCentresRepository.save(pcCentres);

    }

    public void updatePcCentres(Integer id, PcCentres pcCentres) {
        PcCentres pcCentres1 = pcCentresRepository.findPcCentreById(id);
        if (pcCentres1 == null) {
            throw new ApiException("PC Centre not found");
        }
       pcCentres1.setCentreName(pcCentres.getCentreName());
        pcCentres1.setLocation(pcCentres.getLocation());
        pcCentres1.setOpeningHours(pcCentres.getOpeningHours());
        pcCentres1.setContactNumber(pcCentres.getContactNumber());
        pcCentres1.setNumberOfPc(pcCentres.getNumberOfPc());
        pcCentres1.setRating(pcCentres.getRating());
        pcCentres1.setApproved(false);
        pcCentresRepository.save(pcCentres1);
    }

    public void deletePcCentres(Integer id) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(id);
        if (pcCentres == null) {
            throw new ApiException("PC Centres not found");
        }
        pcCentresRepository.delete(pcCentres);
    }
}
