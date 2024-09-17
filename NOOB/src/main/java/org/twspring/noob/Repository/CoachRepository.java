package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Coach;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Integer> {

     Coach findCoachById(Integer id);

     List<Coach> findByHourlyRateBetween(Integer minRate, Integer maxRate);


}
