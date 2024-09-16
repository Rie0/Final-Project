package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PcCentresRepository pcCentresRepository;
    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
    private final SubscribeByRepository subscribeByRepository;
private final  VendorRepository vendorRepository;
private final ZoneRepository zoneRepository;

    public List<Subscription> getAllsubscription() {
        return subscriptionRepository.findAll();
    }


    public void addsubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);

    }

    public void updatesubScription(Integer id, Subscription subscription) {
        Subscription subscription1 = subscriptionRepository.findSubscriptionById(id);
        if (subscription1 == null) {
            throw new ApiException("subscription not found");
        }
     subscription1.setSubscriptionTie(subscription.getSubscriptionTie());
        subscription1.setSubscriptionHours(subscription.getSubscriptionHours());
        subscription1.setPrice(subscription.getPrice());
        subscription1.setMembers(subscription.getMembers());
subscriptionRepository.save(subscription1);    }

    public void deleteSubscription(Integer id) {
        Subscription subscription = subscriptionRepository.findSubscriptionById(id);
        if (subscription == null) {
            throw new ApiException("subscription not found");
        }
        subscriptionRepository.delete(subscription);
    }

    //============

    public List<Subscription> getsubscriptionbyPcCentreId(Integer pcCentreId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCentreId);
        if (pcCentres == null) {
            throw new ApiException("PC Centre not found");

        }
        return subscriptionRepository.findSubscriptionByPcCentresId(pcCentreId);
    }



    public void subscribePlayerToSubscription(Integer playerId, Integer subscriptionId,Integer zoneId) {

        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("player not found");
        }

        Zone zone=zoneRepository.findZoneById(zoneId);
        if (zone == null) {
            throw new ApiException("zone not found");
        }




        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null) {
            throw new ApiException("subscription not found");
        }





        SubscripeBy subscripeBy = new SubscripeBy();
        subscripeBy.setPlayer(player);
        subscripeBy.setSubscription(subscription);
        subscripeBy.setStartDate(new Date());
        subscripeBy.setRemainingHours(subscription.getSubscriptionHours());
        subscripeBy.setStatus(true);

        subscribeByRepository.save(subscripeBy);
    }

}
