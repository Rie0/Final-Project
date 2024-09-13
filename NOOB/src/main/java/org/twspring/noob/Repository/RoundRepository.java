package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Round;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
    Round findRoundById(Integer id);
}
