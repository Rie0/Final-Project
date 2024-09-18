package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // Find a review by its ID
    @Query("SELECT r FROM Review r WHERE r.id = ?1")
    Review findReviewById(Integer id);

    // Find all reviews by a player ID
    @Query("SELECT r FROM Review r WHERE r.player.id = ?1")
    List<Review> findReviewsByPlayerId(Integer playerId);

    // Find all reviews by a coach ID
    @Query("SELECT r FROM Review r WHERE r.coach.id = ?1")
    List<Review> findReviewsByCoachId(Integer coachId);

    // Find all reviews by a coaching session ID
    @Query("SELECT r FROM Review r WHERE r.coachingSession.id = ?1")
    List<Review> findReviewsByCoachingSessionId(Integer coachingSessionId);

    // Find all reviews by both player ID and coach ID
    @Query("SELECT r FROM Review r WHERE r.player.id = ?1 AND r.coach.id = ?2")
    List<Review> findReviewsByPlayerIdAndCoachId(Integer playerId, Integer coachId);

    List<Review> findByPlayer(Player player);

    List<Review> findByCoachId(Integer coachId);

}
