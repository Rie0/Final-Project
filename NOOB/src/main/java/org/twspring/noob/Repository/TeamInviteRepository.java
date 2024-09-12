package org.twspring.noob.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.TeamInvite;

import java.util.List;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Integer> {
    TeamInvite findTeamInviteById(Integer id);

    //EXTRA
    List<TeamInvite> findTeamInvitesByTeamId(Integer teamId);
    List<TeamInvite> findTeamInvitesByPlayerId(Integer playerId);
}
