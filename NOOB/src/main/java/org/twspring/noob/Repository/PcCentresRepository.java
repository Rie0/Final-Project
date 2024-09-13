package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.PcCentres;

@Repository
public interface PcCentresRepository extends JpaRepository<PcCentres, Integer> {

    PcCentres findPcCentreById(Integer id);
}
