package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Bracket;

@Repository
public interface BracketRepository extends JpaRepository<Bracket, Integer> {
    Bracket findBracketById(Integer id);
}
