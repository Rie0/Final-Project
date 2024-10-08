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
////Hassan Alzahrani
@Service
@RequiredArgsConstructor
public class PcCentresService {

    private final PcCentresRepository pcCentresRepository;
    private final VendorRepository vendorRepository;
    private final AuthRepository authRepository;;

    public List<PcCentres> getAllPcCentres() {
        return pcCentresRepository.findAll();
    }
    //CRUD
    ////Hassan Alzahrani
    public void addPcCentres(PcCentres pcCentres, Integer vendorID) {
        Vendor vendor = vendorRepository.findVendorById(vendorID);
        pcCentres.setApproved(false);
      pcCentres.setVendor(vendor);
      pcCentresRepository.save(pcCentres);

    }
    //CRUD
    ////Hassan Alzahrani
    public void updatePcCentres(Integer id, PcCentres pcCentres,Integer vendorId) {
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        PcCentres pcCentres1 = pcCentresRepository.findPcCentreById(id);
        if (pcCentres1 == null) {
            throw new ApiException("PC Centre not found");
        }
     if (vendor == null) {
         throw new ApiException("Vendor with ID " + vendorId + " not found");
     }
     if (vendorId!=pcCentres1.getVendor().getId()) {
         throw new ApiException("Vendor with ID " + vendorId + " is changed");
     }
        pcCentres1.setCentreName(pcCentres.getCentreName());
        pcCentres1.setLocation(pcCentres.getLocation());
        pcCentres1.setOpeningHours(pcCentres.getOpeningHours());
        pcCentres1.setContactNumber(pcCentres.getContactNumber());
        pcCentres1.setNumberOfPc(pcCentres.getNumberOfPc());
        pcCentres1.setRating(pcCentres.getRating());



        pcCentresRepository.save(pcCentres1);
    }
    //CRUD
    ////Hassan Alzahrani
    public void deletePcCentres(Integer id,Integer vendorId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(id);
        if (pcCentres == null) {
            throw new ApiException("PC Centres not found");
        }
        if (pcCentres.getVendor().getId() != pcCentres.getVendor().getId()) {
            throw new ApiException("PC Centres don't match");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with ID " + vendorId + " not found");
        }
        if (vendorId!=pcCentres.getVendor().getId()) {
            throw new ApiException("Vendor with ID " + vendorId + " is changed");
        }
        pcCentresRepository.delete(pcCentres);
    }

    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public List<PcCentres> getPcCentresByVendorID(Integer vendorId) {
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with ID " + vendorId + " not found");
        }
        if (vendor.getPcCentres() == null) {
            throw new ApiException("PC Centres not found");
        }


        return pcCentresRepository.getPcCentresByVendorId(vendorId);

    }


    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public void adminAprovedPcCenter(Integer pcCenterId) {

                PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCenterId);
                if (pcCentres == null) {
                    throw new ApiException("PC Centre not found");
                } else {

                    pcCentres.setApproved(true);
                    pcCentresRepository.save(pcCentres);
                }

        }

    //EXTRA ENDPOINT
////Hassan Alzahrani
    public List<PcCentres> getPcCentresByRating(Integer rating) {
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

    public List<PcCentres> getPcCentreByLocation(String location) {
        if (location == null) {
            throw new ApiException("Location not found");
        }
        return pcCentresRepository.findPcCentresByLocation(location);
    }

    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public List<PcCentres>getApprovedPcCentres(){
  return pcCentresRepository.findPcCentresByApprovedTrue();
    }
    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public List<PcCentres>getAllNotApprovedPcCentre(){
        return pcCentresRepository.findPcCentresByApprovedFalse();
    }


}
