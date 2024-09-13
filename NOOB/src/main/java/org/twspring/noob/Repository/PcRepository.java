package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.PC;

@Repository
public interface PcRepository extends JpaRepository<PC, Integer> {

    PC findPCById(Integer id);
}
