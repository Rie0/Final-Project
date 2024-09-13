package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Subscription;
import org.twspring.noob.Repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;


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
}
