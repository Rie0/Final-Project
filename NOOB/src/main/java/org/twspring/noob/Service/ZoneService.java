package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Subscription;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Model.Zone;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.SubscriptionRepository;
import org.twspring.noob.Repository.VendorRepository;
import org.twspring.noob.Repository.ZoneRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ZoneService {




private final ZoneRepository zoneRepository;
private final PcCentresRepository pcCentresRepository;
private final VendorRepository vendorRepository;
private final SubscriptionRepository subscriptionRepository;



    public List<Zone> getAllPcZone() {
        return zoneRepository.findAll();
    }


    public void addZone(Zone zone,Integer centre_id,Integer vendor_id) {
        PcCentres pcCentres =pcCentresRepository.findPcCentreById(centre_id);
        Vendor vendor = vendorRepository.findVendorById(vendor_id);
        if (vendor == null) {
            throw new ApiException("Vendor with id " + vendor_id + " not found");
        }
        if(pcCentres == null) {
            throw new ApiException("PcCentre not found");


        }
        if (vendor.getId()!=pcCentres.getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }

        zone.setPcCentre(pcCentres);
      zone.setAvailable(false);
        zoneRepository.save(zone);

    }

    public void updateZone(Integer id, Zone zone) {
        Zone zone1 = zoneRepository.findZoneById(id);
        if (zone1 == null) {
            throw new ApiException("Zone not found");
        }
    zone1.setZoneCapacity(zone1.getZoneCapacity());
        zone1.setZoneType(zone1.getZoneType());
        zone1.setZoneType(zone.getZoneType());
        zone1.setAvailable(zone.isAvailable());
        zoneRepository.save(zone1);
    }

    public void deleteZone(Integer id) {
        Zone zone = zoneRepository.findZoneById(id);
        if (zone == null) {
            throw new ApiException("Zone not found");
        }
        zoneRepository.delete(zone);
    }

    public List<Zone> getZoneByPcCentre(Integer pcCentreId){
        PcCentres pcCentre = pcCentresRepository.findPcCentreById(pcCentreId);
        if(pcCentre == null) {
            throw new ApiException("PcCentre not found");
        }
   return zoneRepository.findZoneByPcCentreId(pcCentreId);
    }


    ////
    public void isAvailableZone(Integer PcCentre, Integer zone_id,Integer vendorId) {
Zone zone = zoneRepository.findZoneById(zone_id);
if (zone == null) {
    throw new ApiException("Zone not found");
}
PcCentres pcCentre = pcCentresRepository.findPcCentreById(PcCentre);
if (pcCentre == null) {
    throw new ApiException("PcCentre not found");
}
Vendor vendor= vendorRepository.findVendorById(vendorId);

//        if (vendor.getId()!=pcCentre.getVendor().getId()) {
//            throw new ApiException("vendor id not match");
//        }
zone.setAvailable(zone.isAvailable());
zone.setPcCentre(pcCentre);
zoneRepository.save(zone);
}




//
//
//
//public void zoneCapacity(Integer PcCentre, Integer zone_id,Integer subscriptionId) {
//        Zone zone = zoneRepository.findZoneById(zone_id);
//        if (zone == null) {
//            throw new ApiException("Zone not found");
//        }
//        PcCentres pcCentres= pcCentresRepository.findPcCentreById(PcCentre);
//        if (pcCentres == null) {
//            throw new ApiException("PcCentre not found");
//        }
//    Subscription subscription=subscriptionRepository.findSubscriptionById(subscriptionId);
//        if (subscription==null)
//            throw new ApiException("Subscription not found");
//        if (zone.getPcCentre().getId()!=pcCentres.getId()) {
//            throw new ApiException("PcCentre id not match");
//        }
//        if (zone.getZoneCapacity()<zone.getSubscription().getMembers());
//        throw new  ApiException("Zone capacity is full");
//
//}

    }


