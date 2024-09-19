package com.eatmate.dao.mybatis.account;

import com.eatmate.domain.dto.AccountTeamDto;
import com.eatmate.domain.dto.TeamDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountTeamDao {

    @Select("SELECT t.team_id, t.team_name, t.description " +
            "FROM Team t " +  // 대소문자 일치 확인
            "JOIN account_team at ON t.team_id = at.team_id " +
            "WHERE at.account_id = #{account_id}")
    List<TeamDto> findTeamsByAccountId(@Param("account_id") Long account_id);

    // 리더 팀 조회
    @Select("SELECT at.account_team_id, at.account_id, at.team_id, at.is_leader, " +
            "t.team_name, t.description " +
            "FROM account_team at " +
            "JOIN Team t ON at.team_id = t.team_id " +
            "WHERE at.account_id = #{account_id} " +
            "AND at.is_leader = TRUE")
    List<AccountTeamDto> findTeamsWhereIsLeader(@Param("account_id") Long account_id);

    // 특정 팀과 유저 간의 관계 삭제
    @Delete("DELETE FROM account_team WHERE account_id = #{account_id} AND team_id = #{team_id}")
    void deleteAccountFromTeam(@Param("account_id") String account_id, @Param("team_id") String team_id);

    // 만약 특정 유저와 관련된 모든 팀 관계를 삭제해야 한다면 (계정 삭제 시)
    @Delete("DELETE FROM account_team WHERE account_id = #{account_id}")
    void deleteByAccountId(@Param("account_id") String accountId);

}
