package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.MasterClass;
import org.twspring.noob.Repository.CoachRepository;
import org.twspring.noob.Repository.MasterClassRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterClassService {


    private final MasterClassRepository masterClassRepository;
    private final CoachRepository coachRepository;

    public List<MasterClass> getAllMasterClasses() {
        return masterClassRepository.findAll();
    }

    public void addMasterClass(MasterClass masterClass) {
        masterClassRepository.save(masterClass);
    }

    public void addMasterClassAssign(MasterClass masterClass, Integer coachId) {
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ApiException("Coach not found"));
        masterClass.setCoach(coach);
        masterClassRepository.save(masterClass);
    }

    public void updateMasterClass(Integer id, MasterClass masterClassDetails) {
        MasterClass masterClass = masterClassRepository.findById(id).orElseThrow(() -> new ApiException("MasterClass not found"));
        masterClass.setTitle(masterClassDetails.getTitle());
        masterClass.setDescription(masterClassDetails.getDescription());
        masterClass.setStartDate(masterClassDetails.getStartDate());
        masterClass.setEndDate(masterClassDetails.getEndDate());
        masterClass.setStatus(masterClassDetails.getStatus());
        masterClassRepository.save(masterClass);
    }

    public void deleteMasterClass(Integer id) {
        MasterClass masterClass = masterClassRepository.findById(id).orElseThrow(() -> new ApiException("MasterClass not found"));
        masterClassRepository.delete(masterClass);
    }

    public void assignCoachIdMasterClass(Integer coachId, Integer masterClassId) {
        MasterClass masterClass = masterClassRepository.findById(masterClassId).orElseThrow(() -> new ApiException("MasterClass not found"));
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ApiException("Coach not found"));
        masterClass.setCoach(coach);
        masterClassRepository.save(masterClass);
    }


}
