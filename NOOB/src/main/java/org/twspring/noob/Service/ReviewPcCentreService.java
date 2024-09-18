package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.ReviewPcCentre;
import org.twspring.noob.Repository.PcCentresRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.ReviewPcCentreRepository;
import org.twspring.noob.Repository.SubscribeByRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewPcCentreService {

    private final PlayerRepository playerRepository;
    private final ReviewPcCentreRepository reviewPcCentreRepository;
    private final PcCentresRepository pcCentresRepository;
    private final SubscribeByRepository subscribeByRepository;


    public List<ReviewPcCentre>getAllReviewPcCentre(){
        return reviewPcCentreRepository.findAll();
    }
    /////
    public void addReviewToPcCentre(Integer playerId, Integer pcCentreId,ReviewPcCentre reviewPcCentre) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

        PcCentres pcCentre = pcCentresRepository.findById(pcCentreId).orElse(null);
        if (pcCentre == null) {
            throw new ApiException("PC Centre not found");
        }

        boolean hasSubscribed = subscribeByRepository.existsByPlayerAndSubscriptionPcCentres(player, pcCentre);
        if (!hasSubscribed) {
            throw new ApiException("Player has not subscribed to this PC Centre and cannot add a review");
        }


        reviewPcCentre.setPlayer(player);
        reviewPcCentre.setPcCentre(pcCentre);

        reviewPcCentreRepository.save(reviewPcCentre);
    }

    public void deleteReview(Integer playerId, Integer reviewId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }


        reviewPcCentreRepository.deleteById(reviewId);
    }

    public void updateReviewPcCentre(Integer playerId, Integer reviewPcCentreId,ReviewPcCentre reviewPcCentre) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        ReviewPcCentre reviewPcCentre1 =reviewPcCentreRepository.findREVPcCentreById(reviewPcCentreId);
        if (reviewPcCentre1 == null) {
            throw new ApiException("ReviewPcCentre not found");
        }
        reviewPcCentre1.setRating(reviewPcCentre.getRating());
        reviewPcCentre1.setPlayer(player);
        reviewPcCentre1.setCommnet(reviewPcCentre.getCommnet());
        reviewPcCentre1.setPcCentre(reviewPcCentre.getPcCentre());
        reviewPcCentreRepository.save(reviewPcCentre1);
    }

    public ReviewPcCentre getReviewCentreByPlayerId(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        return reviewPcCentreRepository.findReviewPcCentreByPlayerId(playerId);

    }


}
