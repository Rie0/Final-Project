package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.ReviewPcCentre;

@Repository
public interface ReviewPcCentreRepository extends JpaRepository<ReviewPcCentre,Integer> {

    ReviewPcCentre findReviewPcCentreById(Integer id);
    ReviewPcCentre findREVPcCentreById(Integer id);
    ReviewPcCentre findByPlayerAndPcCentre(Player player, PcCentres pcCentre);
    ReviewPcCentre findReviewPcCentreByPlayerId(Integer playerId);


}
