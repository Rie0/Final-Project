package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeByService {

    private final SubscribeByRepository subscribeByRepository;
    private final AuthRepository authRepository;
    private final PlayerRepository playerRepository;
    private final PcCentresRepository pcCentresRepository;
    private final ZoneRepository zoneRepository;
private final VendorRepository vendorRepository;

    public List<SubscripeBy> getAllsubscribeBy() {
        return subscribeByRepository.findAll();
    }


    public void addSubscripeBy(SubscripeBy subscripeBy,Integer vendorId,Integer pcCenterID,Integer zoneId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCenterID);
        Zone zone=zoneRepository.findZoneById(zoneId);
        Vendor vendor=vendorRepository.findVendorById(vendorId);
        if (vendor==null){
            throw new ApiException("vendor not found");
        }
        if(pcCentres==null) {
            throw new ApiException("pcCentre not found");
        }
        if(zone==null) {
            throw new ApiException("zone not found");
        }
        if (vendor.getId()!=pcCentres.getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }

        subscribeByRepository.save(subscripeBy);

    }

    public void updateSubscribeBy(Integer id, SubscripeBy subscripeBy) {
        SubscripeBy subscripeBy1 = subscribeByRepository.findSubscripeBIESById(id);
        if (subscripeBy1 == null) {
            throw new ApiException("Subscribe By not found");
        }
        subscripeBy1.setSubscription(subscripeBy.getSubscription());
    }

    public void deleteSubscripeBy(Integer id) {
        SubscripeBy subscripeBy = subscribeByRepository.findSubscripeBIESById(id);
        if (subscripeBy == null) {
            throw new ApiException("Subscribe By not found");
        }
        subscribeByRepository.delete(subscripeBy);
    }
//////
    public List<SubscripeBy> getSubscribedByPlayerId(Integer playerId) {
        Player player=playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

return subscribeByRepository.findSubscripeBIESByPlayerId(playerId);
    }

public void reduceSubscribedByRemainingHours(Integer subscribeById, Integer playedHours,Integer vendorId) {
       SubscripeBy subscripeBy=subscribeByRepository.findSubscripeBIESById(subscribeById);
       if (subscripeBy == null) {
           throw new ApiException("Subscribe By not found");

       }


       if (playedHours>subscripeBy.getRemainingHours()) {
           subscripeBy.setStatus(false);
           subscripeBy.setRemainingHours(0);
           throw new ApiException("you used your remaining hours");
       }

       if (subscripeBy.getEndDate().after(subscripeBy.getEndDate())) {
          throw new ApiException("your subscription is ended");
       }

       subscripeBy.setRemainingHours(subscripeBy.getRemainingHours()-playedHours);
subscribeByRepository.save(subscripeBy);
   }




       }







