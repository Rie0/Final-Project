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

////Hassan Alzahrani
@Service
@RequiredArgsConstructor
public class ReviewPcCentreService {

    private final PlayerRepository playerRepository;
    private final ReviewPcCentreRepository reviewPcCentreRepository;
    private final PcCentresRepository pcCentresRepository;
    private final SubscribeByRepository subscribeByRepository;

    //CRUD
    ////Hassan Alzahrani
    public List<ReviewPcCentre> getAllReviewPcCentre() {
        return reviewPcCentreRepository.findAll();
    }

    //CRUD
    ////Hassan Alzahrani
    public void addReviewToPcCentre(Integer playerId, Integer pcCentreId, ReviewPcCentre reviewPcCentre) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

        PcCentres pcCentre = pcCentresRepository.findPcCentreById(pcCentreId);
        if (pcCentre == null) {
            throw new ApiException("PcCentre not found");
        }

        boolean hasSubscribed = subscribeByRepository.existsByPlayerAndSubscriptionPcCentres(player, pcCentre);
        if (!hasSubscribed) {
            throw new ApiException("Player has not subscribed to this PC Centre and cannot add a review");
        }

        reviewPcCentre.setPlayer(player);
        reviewPcCentre.setPcCentre(pcCentre);
        reviewPcCentreRepository.save(reviewPcCentre);
    }

    //CRUD
    ////Hassan Alzahrani
    public void deleteReview(Integer playerId, Integer reviewId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }


        reviewPcCentreRepository.deleteById(reviewId);
    }

    //CRUD
    ////Hassan Alzahrani
    public void updateReviewPcCentre(Integer playerId, Integer reviewPcCentreId, ReviewPcCentre reviewPcCentre) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        ReviewPcCentre reviewPcCentre1 = reviewPcCentreRepository.findREVPcCentreById(reviewPcCentreId);
        if (reviewPcCentre1 == null) {
            throw new ApiException("ReviewPcCentre not found");
        }
        reviewPcCentre1.setRating(reviewPcCentre.getRating());
        reviewPcCentre1.setPlayer(player);
        reviewPcCentre1.setCommnet(reviewPcCentre.getCommnet());
        reviewPcCentre1.setPcCentre(reviewPcCentre.getPcCentre());
        reviewPcCentreRepository.save(reviewPcCentre1);
    }

    //EXTRA ENDPOINT
    ////Hassan Alzahrani
    public ReviewPcCentre getReviewCentreByPlayerId(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        return reviewPcCentreRepository.findReviewPcCentreByPlayerId(playerId);

    }


}
