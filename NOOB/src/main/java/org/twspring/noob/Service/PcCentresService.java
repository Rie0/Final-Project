package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.VendorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PcCentresService {

    private final PcCentresRepository pcCentresRepository;
    private final VendorRepository vendorRepository;
    private final AuthRepository authRepository;;


    public List<PcCentres> getAllPcCentres() {
        return pcCentresRepository.findAll();
    }


    public void addPcCentres(PcCentres pcCentres,Integer vendorID) {
        Vendor vendor = vendorRepository.findVendorById(vendorID);
        if (vendor == null) {
            throw new ApiException("Vendor with ID " + vendorID + " not found");
        }


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

    public List<PcCentres>getPcCentresByVendorID(Integer vendorId) {
Vendor vendor=vendorRepository.findVendorById(vendorId);
if (vendor==null){
    throw new ApiException("Vendor with ID " + vendorId + " not found");
}
if (vendor.getPcCentres()==null){
    throw new ApiException("PC Centres not found");
}

        return pcCentresRepository.getPcCentresByVendorId(vendorId);

    }

    //======

    public void adminAprovedPcCenter(Integer adminId, Integer pcCenterId){
        User u = authRepository.findUserById(adminId);
        if (u==null){
            throw new ApiException("User not found");
        }else {
            if (u.getRole().equalsIgnoreCase("ADMIN")) {
                PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCenterId);
                if (pcCentres == null) {
                    throw new ApiException("PC Centre not found");
                }else {

                    pcCentres.setApproved(true);
                    pcCentresRepository.save(pcCentres);
                }
            }else {
                throw new ApiException("You are not an admin");
            }
        }
    }

    ///=============

    public List<PcCentres>getPcCentresByRating(Integer rating){
if (rating == null) {
    throw new ApiException("Rating not found");
}
        return pcCentresRepository.findPcCentresByRating(rating);
    }

    public List<PcCentres> getPcCentresByRatingRange(int minRating, int maxRating) {
        return pcCentresRepository.findByRatingBetween(minRating, maxRating);
    }

    public PcCentres getPcCentreByCentreName(String centreName) {
        PcCentres pcCentres = pcCentresRepository.findPcCentresByCentreName(centreName);
        if (pcCentres == null) {
            throw new ApiException("PC Centre not found");
        }
        return pcCentres;
    }

    public List<PcCentres>getPcCentreByLocation(String location) {
        if (location == null) {
        throw new ApiException("Location not found");
        }
        return pcCentresRepository.findPcCentresByLocation(location);
    }
}
