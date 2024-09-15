package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.MasterClass;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Repository.CoachRepository;
import org.twspring.noob.Repository.MasterClassRepository;
import org.twspring.noob.Repository.PlayerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterClassService {


    private final MasterClassRepository masterClassRepository;

    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;

    // CRUD get all
    public List<MasterClass> getAllMasterClasses() {
        return masterClassRepository.findAll();
    }

    // CRUD register
    public void addMasterClass(MasterClass masterClass, Integer coachId) {
        Coach coach = coachRepository.findCoachById(coachId);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        masterClass.setCoach(coach);
        masterClassRepository.save(masterClass);
    }

    // CRUD update
    public void updateMasterClass(Integer id, MasterClass masterClassDetails) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(id);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        masterClass.setTitle(masterClassDetails.getTitle());
        masterClass.setDescription(masterClassDetails.getDescription());
        masterClass.setStartDate(masterClassDetails.getStartDate());
        masterClass.setEndDate(masterClassDetails.getEndDate());
        masterClass.setStatus(masterClassDetails.getStatus());
        masterClassRepository.save(masterClass);
    }

    // CRUD delete
    public void deleteMasterClass(Integer id) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(id);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        masterClassRepository.delete(masterClass);
    }

    // EXTRA endpoint: getting a master class by id
    public MasterClass getMasterClassById(Integer id) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(id);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        return masterClass;
    }

    // EXTRA endpoint: getting master classes by coach id
    public List<MasterClass> getMasterClassesByCoachId(Integer coachId) {
        return masterClassRepository.findMasterClassesByCoachId(coachId);
    }

    // EXTRA endpoint: getting master classes by coach id and status
    public List<MasterClass> getMasterClassesByCoachIdAndStatus(Integer coachId, String status) {
        return masterClassRepository.findMasterClassesByCoachIdAndStatus(coachId, status);
    }

    // EXTRA endpoint: starting a master class
    public void startMasterClass(Integer masterClassId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        masterClass.setStatus("Active");
        masterClassRepository.save(masterClass);
    }

    // EXTRA endpoint: closing a master class
    public void closeMasterClass(Integer masterClassId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        masterClass.setStatus("Closed");
        masterClassRepository.save(masterClass);
    }

    // EXTRA endpoint: canceling a master class
    public void cancelMasterClass(Integer masterClassId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        masterClass.setStatus("Canceled");
        // We need to do the refund here after my colleague finish off
        masterClassRepository.save(masterClass);
    }


    // EXTRA endpoint: joining a master class
    public void joinMasterClass(Integer masterClassId, Integer playerId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        masterClass.getPlayers().add(player);
        masterClassRepository.save(masterClass);
    }


    // EXTRA endpoint: leaving a master class
    public void leaveMasterClass(Integer masterClassId, Integer playerId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (!masterClass.getPlayers().remove(player)) {
            throw new ApiException("Player is not part of the masterclass");
        }
        masterClassRepository.save(masterClass);
    }


    // EXTRA endpoint: kicking a player from a master class
    public void kickPlayerFromMasterClass(Integer masterClassId, Integer playerId) {
        MasterClass masterClass = masterClassRepository.findMasterClassById(masterClassId);
        if (masterClass == null) {
            throw new ApiException("MasterClass not found");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (!masterClass.getPlayers().remove(player)) {
            throw new ApiException("Player is not part of the masterclass");
        }
        masterClassRepository.save(masterClass);
    }
}
