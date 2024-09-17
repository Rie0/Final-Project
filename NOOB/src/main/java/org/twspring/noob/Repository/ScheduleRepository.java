package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Schedule;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    Schedule findScheduleById(Integer id);


    List<Schedule> findSchedulesByCoachId(Integer coachId);
//    @Query("SELECT s FROM Schedule s WHERE s.coach.id = ?1 AND s.availableTime = ?2 AND s.isAvailable = true")
//    Schedule findAvailableScheduleByCoachAndTime(Integer coachId, LocalDateTime scheduledTime);

}
