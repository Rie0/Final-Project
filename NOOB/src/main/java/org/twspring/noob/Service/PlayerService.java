package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.DTO.PlayerProfileDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.ParticipantRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TeamInviteRepository;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService { //RAFEEF
    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
    private final TeamInviteRepository teamInviteRepository;
    private final ParticipantRepository participantRepository;
    private final CoachingSessionRepository coachingSessionRepository;
    private final ReviewRepository reviewRepository;
    private final CoachRepository coachRepository;

    //START OF CRUD
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public void registerPlayer(PlayerDTO playerDTO) {
        //String hash= new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        User user = new User();
        user.setUsername(playerDTO.getUsername());
        user.setPassword(playerDTO.getPassword());
        user.setEmail(playerDTO.getEmail());
        user.setPhoneNumber(playerDTO.getPhoneNumber());
        user.setBirthday(playerDTO.getBirthday());
        user.setAge(Period.between(user.getBirthday(), LocalDate.now()).getYears());
        if (user.getAge()<13){
            throw new ApiException("Players under the age of 13 are prohibited from registering in our system");
        }
        if ((user.getAge()<18)&&playerDTO.getParentApproval()==null){
            throw new ApiException("Minors must include a parental approval before registering");
        }
        user.setRole("PLAYER");
        authRepository.save(user);

        Player player = new Player();
        player.setUser(user);
        playerRepository.save(player);

    }
    public void updatePlayer(Integer playerId,PlayerDTO playerDTO) {
        Player oldPlayer = playerRepository.findPlayerById(playerId);
        User oldUser = oldPlayer.getUser();

        String hash= new BCryptPasswordEncoder().encode(playerDTO.getPassword());

        oldUser.setEmail(playerDTO.getEmail());
        oldUser.setPassword(hash);
        oldUser.setPhoneNumber(playerDTO.getPhoneNumber());
        authRepository.save(oldUser);
    }

    public void deletePlayer(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        for (Participant participant : player.getParticipants()) {
            participant.setPlayer(null);
            player.getParticipants().remove(participant);
            playerRepository.save(player);
            participantRepository.delete(participant);
        }
        authRepository.deleteById(playerId);
    }
    //END OF CRUD

    //GENERAL EXTRA
    public Player getPlayerById(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {throw new ApiException("Player not found");}
        return player;
    }

    public PlayerProfileDTO getPlayerProfileById(Integer playerId) {
        Player player= playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (player.getTeam()==null){
            return new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),"Not in team.",player.getUser().getJoinedAt());
        }
        return new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),player.getTeam().getUser().getUsername(),player.getUser().getJoinedAt());
    }

    public List<PlayerProfileDTO> getPlayerProfilesByUsernameContaining(String username) {
        List<Player> foundPlayer= playerRepository.findPlayersByUserUsernameContaining(username);
        List<PlayerProfileDTO> foundPlayerProfiles= new ArrayList<>();
        for (Player player : foundPlayer) {
            if (player.getTeam()==null){
                PlayerProfileDTO playerProfileDTO = new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),"Not in team.",player.getUser().getJoinedAt());
                foundPlayerProfiles.add(playerProfileDTO);
                continue;
            }
            PlayerProfileDTO playerProfileDTO = new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),player.getTeam().getUser().getUsername(),player.getUser().getJoinedAt());
            foundPlayerProfiles.add(playerProfileDTO);
        }
        return foundPlayerProfiles;
    }


    public void updateBio(Integer playerId, String bio) {
        Player player = playerRepository.findPlayerById(playerId);

        player.setBio(bio);
        playerRepository.save(player);
    }

    //PLAYER INVITES
    public List<TeamInvite> getInvitesByPlayerId(Integer playerId) {
        return teamInviteRepository.findTeamInvitesByPlayerId(playerId);
    }

    public void acceptInvite(Integer playerId, Integer teamInviteId) {
        TeamInvite invite = teamInviteRepository.findTeamInviteById(teamInviteId);
        Player player = playerRepository.findPlayerById(playerId);

        if (invite==null){
            throw new ApiException("No invite found");
        }
        if (!invite.getPlayer().equals(player)){
            throw new ApiException("Player doesn't own this invite");
        }
        if (player.getTeam()!=null){
            throw new ApiException("You are already in a team, leave your current team to accept this invite");
        }
        if (invite.getStatus()!= TeamInvite.Status.PENDING){
            throw new ApiException("Only pending invites can be accepted");
        }
        invite.setStatus(TeamInvite.Status.APPROVED);
        teamInviteRepository.save(invite);

        //ADD PLAYER TO TEAM
        player.setTeam(invite.getTeam());
        player.setJoinedTeamAt(LocalDateTime.now());
        playerRepository.save(player);
    }

    public void declineInvite(Integer playerId, Integer teamInviteId) {
        TeamInvite invite = teamInviteRepository.findTeamInviteById(teamInviteId);
        Player player = playerRepository.findPlayerById(playerId);

        if (invite==null){
            throw new ApiException("No invite found");
        }
        if (!invite.getPlayer().equals(player)){
            throw new ApiException("Player doesn't own this invite");
        }
        if (invite.getStatus()!= TeamInvite.Status.PENDING){
            throw new ApiException("Only pending invites can be declined");
        }

        invite.setStatus(TeamInvite.Status.DECLINED);
        teamInviteRepository.save(invite);
    }

    //TEAM
    public void leaveTeam(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player.getTeam()==null){
            throw new ApiException("You are not in a team");
        }
        player.setTeam(null);
        player.setJoinedTeamAt(null);
        playerRepository.save(player);
    }

    public void addReview(Integer playerId, Integer coachId, Integer coachingSessionId, String comment, Float rating) {

        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(coachingSessionId);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        }

        // Check if the player and coach are associated with the coaching session
        if (!coachingSession.getPlayer().getId().equals(playerId) || !coachingSession.getCoach().getId().equals(coachId)) {
            throw new ApiException("Player and coach are not associated with this coaching session");
        }

        // Ensure the coaching session status is "ended"
        if (!"ended".equalsIgnoreCase(coachingSession.getStatus())) {
            throw new ApiException("Coaching session is not ended yet");
        }

        // Create and save the review
        Review review = new Review();
        review.setPlayer(coachingSession.getPlayer());
        review.setCoach(coachingSession.getCoach());
        review.setCoachingSession(coachingSession);
        review.setComment(comment);
        review.setRating(rating);

        reviewRepository.save(review);
    }

    // CRUD get all
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // CRUD update
    public void updateReview(Integer id, Review review) {
        Review existingReview = reviewRepository.findReviewById(id);
        if (existingReview == null) {
            new ApiException("Review not found");
        }

        Player player = playerRepository.findPlayerById(review.getPlayer().getId());
        if (player == null) {
               new ApiException("Player not found");
        }
        Coach coach = coachRepository.findCoachById(review.getCoach().getId());

        if (coach == null) {
            new ApiException("Coach not found");
        }
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(review.getCoachingSession().getId());
        if (coachingSession == null){
            new ApiException("Coaching session not found");
        }

        existingReview.setPlayer(player);
        existingReview.setCoach(coach);
        existingReview.setCoachingSession(coachingSession);
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());

        reviewRepository.save(existingReview);
    }

    // CRUD delete
    public void deleteReview(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            new ApiException("Review not found");
        }
        reviewRepository.delete(review);
    }


    public List<Review> getReviewsForPlayer(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        return reviewRepository.findByPlayer(player);
    }

    // EXTRA endpoint: getting a review by id
    public Review getReviewById(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ApiException("Review not found");
        }
        return review;
    }



}
