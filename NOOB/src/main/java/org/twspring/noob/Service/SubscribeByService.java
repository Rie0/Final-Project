package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.util.Date;
import java.util.List;
////Hassan Alzahrani
@Service
@RequiredArgsConstructor
public class SubscribeByService {

    private final SubscribeByRepository subscribeByRepository;
    private final AuthRepository authRepository;
    private final PlayerRepository playerRepository;
    private final PcCentresRepository pcCentresRepository;
    private final ZoneRepository zoneRepository;
    private final VendorRepository vendorRepository;
    private final SubscriptionRepository subscriptionRepository;
    //CRUD
    ////Hassan Alzahrani
    public List<SubscripeBy> getAllsubscribeBy() {
        return subscribeByRepository.findAll();
    }
    //CRUD
    ////Hassan Alzahrani
    public void addSubscripeBy(SubscripeBy subscripeBy, Integer vendorId, Integer pcCenterID, Integer zoneId, Integer playerId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCenterID);
        Zone zone = zoneRepository.findZoneById(zoneId);
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (vendor == null) {
            throw new ApiException("vendor not found");
        }
        if (pcCentres == null) {
            throw new ApiException("pcCentre not found");
        }
        if (zone == null) {
            throw new ApiException("zone not found");
        }
        if (vendor.getId() != pcCentres.getVendor().getId()) {
            throw new ApiException("vendor id not match");
        }
        subscripeBy.setPlayer(player);
        subscribeByRepository.save(subscripeBy);

    }
    //CRUD
    ////Hassan Alzahrani
    public void updateSubscribeById(Integer id, SubscripeBy subscripeBy) {
        SubscripeBy subscripeBy1 = subscribeByRepository.findSubscripeBIESById(id);
        if (subscripeBy1 == null) {
            throw new ApiException("Subscribe By not found");
        }
        subscripeBy1.setSubscription(subscripeBy.getSubscription());
    }
    //CRUD
    ////Hassan Alzahrani
    public void deleteSubscripeById(Integer id) {
        SubscripeBy subscripeBy = subscribeByRepository.findSubscripeBIESById(id);
        if (subscripeBy == null) {
            throw new ApiException("Subscribe By not found");
        }
        subscribeByRepository.delete(subscripeBy);
    }

    //EXTRA ENDPOINT
    public List<SubscripeBy> getSubscribedByPlayerId(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

        return subscribeByRepository.findSubscripeBIESByPlayerId(playerId);
    }

    //EXTRA ENDPOINT
    ////Hassan Alzahrani

    public void subscribePlayerToSubscription(Integer playerId, Integer subscriptionId, Integer zoneId,
                                              int playerMembers, String coupan, String name) {

        Player player = playerRepository.findPlayerById(playerId);
        if (player == null)
            throw new ApiException("Player not found");

        Zone zone = zoneRepository.findZoneById(zoneId);
        if (zone == null)
            throw new ApiException("Zone not found");

        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null)
            throw new ApiException("Subscription not found");

        SubscripeBy subscripeBy = new SubscripeBy();
        subscripeBy.setPlayer(player);
        subscripeBy.setSubscription(subscription);
        subscripeBy.setStartDate(new Date());
        subscripeBy.setRemainingHours(subscription.getSubscriptionHours());
        subscripeBy.setStatus(true);
        subscripeBy.setPlayerMembers(playerMembers);

        if (playerMembers > 2) {
            double originalPrice = subscription.getPrice();
            double discountedPrice = originalPrice * 0.80;
            subscription.setPrice(discountedPrice);
        }
        if (coupan.equalsIgnoreCase("94")) {
            subscription.setPrice(subscription.getPrice() - 94);
        }
        if (name.equalsIgnoreCase("pro")) {
            subscription.setSubscriptionNmae("pro");
            subscription.setPrice(subscription.getSubscriptionHours() + 10);

        }
        if (name.equalsIgnoreCase("basic")) {
            subscription.setSubscriptionNmae("basic");
            subscription.setPrice(subscription.getSubscriptionHours() + 2);
        }
subscripeBy.setPlayer(player);
        subscribeByRepository.save(subscripeBy);
    }

    //EXTRA ENDPOINT
////Hassan Alzahrani
    public void playerReturnSubscription(Integer subscripeById) {
        SubscripeBy subscripeBy = subscribeByRepository.findSubscripeBIESById(subscripeById);
        if (subscripeBy == null) {
            throw new ApiException("subscribBy id not found");
        }

        long timeElapsed = new Date().getTime() - subscripeBy.getStartDate().getTime();
        long twentyFiveMinutesInMillis = 25 * 60 * 1000;

        if (timeElapsed > twentyFiveMinutesInMillis) {
            throw new ApiException("Cannot return subscription after 25 minutes");
        }

        subscripeBy.setEndDate(new Date());
        subscripeBy.setStatus(false);

        subscribeByRepository.save(subscripeBy);

        Subscription subscription = subscripeBy.getSubscription();
        if (subscription != null) {
            subscription.setPrice(subscription.getPrice() + calculateRefundAmount(subscripeBy)); // Adjust subscription

            subscriptionRepository.save(subscription);
        }
    }

    private double calculateRefundAmount(SubscripeBy subscripeBy) {
        return subscripeBy.getSubscription().getPrice();
    }

}
