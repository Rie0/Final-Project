package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.PcRepository;
import org.twspring.noob.Repository.VendorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PcService {


    private final PcRepository pcRepository;
    private final AuthRepository authRepository;
private final VendorRepository vendorRepository;
private final PcCentresRepository pcCentresRepository;

    public List<PC> getAllPc() {
        return pcRepository.findAll();
    }

//////////
    public void addPc(PC pc,Integer pcCenterId,Integer vendorId) {
        Vendor vendor=vendorRepository.findVendorById(vendorId);
        PcCentres pcCentres=pcCentresRepository.findPcCentreById(pcCenterId);
        if(pcCentres==null) {
            throw new ApiException("Pc Centre Not Found");
        }
        if(vendor==null) {
            throw new ApiException("Vendor Not Found");
        }
        if (vendor.getId()!=pcCentres.getVendor().getId()){
            throw new ApiException("Vendor Id Not Match");
        }



    }

    public void updatePc(Integer pcId, PC pc, Integer vendorId ) {
        PC pc1 = pcRepository.findPCById(pcId);
        if (pc1 == null) {
            throw new ApiException("PC not found");
        }
        Vendor vendor=vendorRepository.findVendorById(vendorId);
        if(vendor==null) {
            throw new ApiException("Vendor Not Found");
        }
if (vendor.getId()!=pc.getPcCentre().getVendor().getId()){
    throw new ApiException("Vendor Id Not Match");
}

        pc1.setAvailable(true);
        pc1.setBrand(pc.getBrand());
        pc1.setModel(pc.getModel());
        pc1.setSpecs(pc.getSpecs());
        pcRepository.save(pc1);
    }

    public void deletePc(Integer pcid,Integer vendorId) {
        PC pc1 = pcRepository.findPCById(pcid);
        if (pc1 == null) {
            throw new ApiException("PC not found");
        }
        Vendor vendor=vendorRepository.findVendorById(vendorId);
        if(vendor==null) {
            throw new ApiException("Vendor Not Found");
        }
        if (vendor.getId()!=pc1.getPcCentre().getVendor().getId()){
            throw new ApiException("Vendor Id Not Match");
        }
        pcRepository.delete(pc1);
    }

}
