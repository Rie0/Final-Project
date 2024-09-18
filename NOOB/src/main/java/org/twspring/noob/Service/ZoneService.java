package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Model.Zone;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.SubscriptionRepository;
import org.twspring.noob.Repository.VendorRepository;
import org.twspring.noob.Repository.ZoneRepository;

import java.util.List;

////Hassan Alzahrani
@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final PcCentresRepository pcCentresRepository;
    private final VendorRepository vendorRepository;
    private final SubscriptionRepository subscriptionRepository;
    //CRUD
    ////Hassan Alzahrani
    public List<Zone> getAllPcZone() {
        return zoneRepository.findAll();
    }

    public void addZone(Zone zone, Integer pcentreIdd, Integer vendorId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcentreIdd);
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with id " + vendorId + " not found");
        }
        if (pcCentres == null) {
            throw new ApiException("PcCentre not found");

        }
        if (vendor.getId() != pcCentres.getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }

        zone.setPcCentre(pcCentres);
        zone.setAvailable(false);
        zoneRepository.save(zone);

    }
    //CRUD
    ////Hassan Alzahrani
    public void updateZone(Integer zoneId, Zone zone, Integer vendorId) {
        Zone zone1 = zoneRepository.findZoneById(zoneId);
        if (zone1 == null) {
            throw new ApiException("Zone not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with id " + vendorId + " not found");
        }
        if (vendor.getId() != zone.getPcCentre().getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }

        zone1.setZoneCapacity(zone1.getZoneCapacity());
        zone1.setZoneType(zone1.getZoneType());
        zone1.setZoneType(zone.getZoneType());
        zoneRepository.save(zone1);
    }
    //CRUD
    ////Hassan Alzahrani
    public void deleteZone(Integer zoneId, Integer vendorId) {
        Zone zone = zoneRepository.findZoneById(zoneId);
        if (zone == null) {
            throw new ApiException("Zone not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with id " + vendorId + " not found");
        }
        if (vendor.getId() != zone.getPcCentre().getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }
        zoneRepository.delete(zone);
    }
    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public List<Zone> getZoneByPcCentre(Integer pcCentreId) {
        PcCentres pcCentre = pcCentresRepository.findPcCentreById(pcCentreId);
        if (pcCentre == null) {
            throw new ApiException("PcCentre not found");
        }
        return zoneRepository.findZoneByPcCentreId(pcCentreId);
    }

    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public void changeZoneStatus(Integer PcCentre, Integer zone_id, Integer vendorId) {
        Zone zone = zoneRepository.findZoneById(zone_id);
        if (zone == null) {
            throw new ApiException("Zone not found");
        }
        PcCentres pcCentre = pcCentresRepository.findPcCentreById(PcCentre);
        if (pcCentre == null) {
            throw new ApiException("PcCentre not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor with id " + vendorId + " not found");
        }
        if (vendor.getId() != zone.getPcCentre().getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }

        zone.setAvailable(true);
        zone.setPcCentre(pcCentre);
        zoneRepository.save(zone);
    }

}


