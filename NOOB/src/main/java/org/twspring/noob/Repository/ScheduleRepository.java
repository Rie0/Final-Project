package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Schedule;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    Schedule findScheduleById(Integer id);

}
