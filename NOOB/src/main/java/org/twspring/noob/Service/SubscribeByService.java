package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.SubscripeBy;
import org.twspring.noob.Repository.SubscribeByRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeByService {

    private final SubscribeByRepository subscribeByRepository;


    public List<SubscripeBy> getAllsubscribeBy() {
        return subscribeByRepository.findAll();
    }


    public void addSubscripeBy(SubscripeBy subscripeBy) {
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
        if (subscripeBy== null) {
            throw new ApiException("Subscribe By not found");
        }
        subscribeByRepository.delete(subscripeBy);
    }
}
