package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.PcCentres;

import java.util.List;

@Repository
public interface PcCentresRepository extends JpaRepository<PcCentres, Integer> {

    PcCentres findPcCentreById(Integer id);

    List<PcCentres>getPcCentresByVendorId(Integer id);
    List<PcCentres>findPcCentresByRating(Integer rating);

    List<PcCentres> findByRatingBetween(int minRating, int maxRating);
PcCentres findPcCentresByCentreName(String centreName);

List<PcCentres>findPcCentresByLocation(String location);

}

