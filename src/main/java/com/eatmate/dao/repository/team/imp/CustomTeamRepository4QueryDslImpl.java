package com.eatmate.dao.repository.team.imp;

import com.eatmate.dao.repository.team.CustomTeamRepository4QueryDsl;


import com.eatmate.domain.entity.user.QAccount;
import com.eatmate.domain.entity.user.QTeam;
import com.eatmate.post.vo.PostPageDto;
import com.eatmate.post.vo.TeamSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class CustomTeamRepository4QueryDslImpl implements CustomTeamRepository4QueryDsl {

    private final JPAQueryFactory queryFactory;

    QTeam team = QTeam.team;

    QAccount account = QAccount.account;

    @Override
    public Page<PostPageDto> searchWithPage(TeamSearchCondition condition, Pageable pageable) {

                List<PostPageDto> query = queryFactory

                .select(Projections.constructor(PostPageDto.class,
                        team.id,
                        team.teamName,
                        team.addressName,
                        team.placeName,
                        //team.members.get(0).account.nickname,
                        team.members.size(),
                        team.createdAt
                ))
                .from(team)
                .where(keywordEq(condition.getKeyword()))
                .orderBy(team.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(team.count())
                .from(team)
                .where(keywordEq(condition.getKeyword()))
                .fetchOne();

        return new PageImpl<>(query, pageable, total);
    }

    private BooleanExpression keywordEq(String keyword){
        if (!StringUtils.hasText(keyword)){
            return null;
        }
        return team.teamName.containsIgnoreCase(keyword)
                .or(team.placeName.containsIgnoreCase(keyword))
                .or(team.addressName.containsIgnoreCase(keyword))
                .or(team.roadAddressName.containsIgnoreCase(keyword))
                ;
    }
}
