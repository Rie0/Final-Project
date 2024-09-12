package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.MasterClass;

@Repository
public interface MasterClassRepository extends JpaRepository<MasterClass, Integer> {

    MasterClass findMasterClassById(Integer id);
}
