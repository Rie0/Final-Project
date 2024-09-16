package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.SubscripeBy;

import java.util.List;

@Repository
public interface SubscribeByRepository extends JpaRepository<SubscripeBy, Integer> {
    SubscripeBy findSubscripeBIESById(Integer id);

   List<SubscripeBy>findSubscripeBIESByPlayerId(Integer id);
}
