package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.MasterClass;

import java.util.List;

@Repository
public interface MasterClassRepository extends JpaRepository<MasterClass, Integer> {

    MasterClass findMasterClassById(Integer id);

    List<MasterClass> findMasterClassesByCoachId(Integer coachId);

    List<MasterClass> findMasterClassesByCoachIdAndStatus(Integer coachId, String status);
}
