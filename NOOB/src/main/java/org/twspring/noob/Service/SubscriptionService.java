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


    public void addsubscription(Subscription subscription,  Integer id, Integer vendorId) {
        PcCentres pcCentres = pcCentresRepository.findPcCentreById(id);
        if (pcCentres == null) {
            throw new ApiException("PC Centre not found");

        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor not found");
        }
        if (vendor.getId()!=subscription.getPcCentres().getVendor().getId()){
            throw new ApiException("you dont have authority");
        }


        subscription.setPcCentres(pcCentres);
        subscriptionRepository.save(subscription);

    }

    public void updatesubScription(Integer subscriptionId, Subscription subscription,Integer vendorId) {
        Subscription subscription1 = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription1 == null) {
            throw new ApiException("subscription not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("vendor not found");
        }
        if (vendor.getId()!=subscription.getPcCentres().getVendor().getId()){
            throw new ApiException("you dont have authority");
        }
        subscription1.setSubscriptionNmae(subscription.getSubscriptionNmae());
        subscription1.setSubscriptionHours(subscription.getSubscriptionHours());
        subscription1.setPrice(subscription.getPrice());
        subscription1.setMembers(subscription.getMembers());
        subscriptionRepository.save(subscription1);    }

    public void deleteSubscription(Integer id,Integer vendorId) {
        Subscription subscription = subscriptionRepository.findSubscriptionById(id);
        if (subscription == null) {
            throw new ApiException("subscription not found");
        }
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("vendor not found");
        }
        if (vendor.getId()!=subscription.getPcCentres().getVendor().getId()){
            throw new ApiException("you dont have authority");
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

}