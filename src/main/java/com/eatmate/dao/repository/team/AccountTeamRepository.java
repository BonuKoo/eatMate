package com.eatmate.dao.repository.team;

import com.eatmate.domain.entity.user.Account;
import com.eatmate.domain.entity.user.AccountTeam;
import com.eatmate.domain.entity.user.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTeamRepository extends JpaRepository<AccountTeam, Long> {

    // account와 team을 기준으로 이미 존재하는지 확인하는 메서드
    Optional<AccountTeam> findByAccountAndTeam(Account account, Team team);
}
