package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

////Hassan Alzahrani
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PcCentresRepository pcCentresRepository;
    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
    private final SubscribeByRepository subscribeByRepository;
    private final VendorRepository vendorRepository;
    private final ZoneRepository zoneRepository;

    //CRUD
    ////Hassan Alzahrani
    public List<Subscription> getAllsubscription() {
        return subscriptionRepository.findAll();
    }

    //CRUD
    ////Hassan Alzahrani
    public void addsubscription(Subscription subscription, Integer pcCentreId, Integer vendorId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCentreId);
        if (pcCentres == null) {
            throw new ApiException("PC Centre not found");

        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor not found");
        }
        if (vendor.getId() != pcCentres.getId()) {
            throw new ApiException("Vendor id mismatch");
        }


        subscription.setPcCentres(pcCentres);
        subscriptionRepository.save(subscription);

    }

    //CRUD
    ////Hassan Alzahrani
    public void updatesubScription(Integer subscriptionId, Subscription subscription, Integer vendorId) {
        Subscription subscription1 = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription1 == null) {
            throw new ApiException("subscription not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("vendor not found");
        }
        if (vendor.getId() != subscription1.getPcCentres().getId()) {
            throw new ApiException("Vendor id mismatch");
        }
        subscription1.setSubscriptionNmae(subscription.getSubscriptionNmae());
        subscription1.setSubscriptionHours(subscription.getSubscriptionHours());
        subscription1.setPrice(subscription.getPrice());
        subscription1.setMembers(subscription.getMembers());
        subscriptionRepository.save(subscription1);
    }
    ////Hassan Alzahrani
    ///CRUD
    public void deleteSubscription(Integer subscriptionId, Integer vendorId) {
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null) {
            throw new ApiException("subscription not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("vendor not found");
        }
        if (vendor.getId() != subscription.getPcCentres().getId()) {
            throw new ApiException("you dont have authority");
        }
        subscriptionRepository.deleteById(subscriptionId);
    }

    //EXTRA ENDPOINT
////Hassan Alzahrani
    public List<Subscription> getsubscriptionbyPcCentreId(Integer pcCentreId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(pcCentreId);
        if (pcCentres == null) {
            throw new ApiException("PC Centre not found");

        }
        return subscriptionRepository.findSubscriptionByPcCentresId(pcCentreId);
    }

}
