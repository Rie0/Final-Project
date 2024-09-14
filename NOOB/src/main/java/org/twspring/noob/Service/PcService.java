package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PcRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PcService {


    private final PcRepository pcRepository;
    private final AuthRepository authRepository;


    public List<PC> getAllPc() {
        return pcRepository.findAll();
    }


    public void addPc(PC pc) {
        pcRepository.save(pc);

    }

    public void updatePc(Integer id, PC pc) {
        PC pc1 = pcRepository.findPCById(id);
        if (pc1 == null) {
            throw new ApiException("PC not found");
        }
        pc1.setAvailable(true);
        pc1.setBrand(pc.getBrand());
        pc1.setModel(pc.getModel());
        pc1.setSpecs(pc.getSpecs());
        pcRepository.save(pc1);
    }

    public void deletePc(Integer id) {
        PC pc1 = pcRepository.findPCById(id);
        if (pc1 == null) {
            throw new ApiException("PC not found");
        }
        pcRepository.delete(pc1);
    }

}
