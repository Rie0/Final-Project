package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Zone;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.ZoneRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ZoneService {




private final ZoneRepository zoneRepository;
private final PcCentresRepository pcCentresRepository;


    public List<Zone> getAllPcZone() {
        return zoneRepository.findAll();
    }


    public void addZone(Zone zone,Integer centre_id) {
        PcCentres pcCentres =pcCentresRepository.findPcCentreById(centre_id);
        if(pcCentres == null) {
            throw new ApiException("PcCentre not found");

        }
        zone.setPcCentre(pcCentres);
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

}
