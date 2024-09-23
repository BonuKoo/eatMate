package com.eatmate.dao.repository.notice.impl;

import com.eatmate.dao.repository.notice.NoticeRepository4QueryDsl;
import com.eatmate.domain.entity.notice.QNotice;
import com.eatmate.domain.entity.user.QAccount;
import com.eatmate.notice.vo.NoticePageForm;
import com.eatmate.notice.vo.NoticeSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

public class NoticeRepository4QueryDslImpl implements NoticeRepository4QueryDsl {

    private final JPAQueryFactory queryFactory;

    public NoticeRepository4QueryDslImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
        
    QNotice notice = new QNotice(QNotice.notice);
    QAccount account = new QAccount(QAccount.account);

    @Override
    public Page<NoticePageForm> searchWithPage(NoticeSearchCondition condition, Pageable pageable) {
        List<NoticePageForm> query = queryFactory.select(Projections.constructor(NoticePageForm.class,
                        notice.id,
                        notice.title,
                        account.nickname,
                        notice.content
                ))
                .from(notice)
                .where(titleEq(condition.getTitle()))
                .orderBy(notice.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(titleEq(condition.getTitle()))
                .fetchOne();

        return new PageImpl<>(query,pageable,total);
    }
    //검색 조건
    private BooleanExpression titleEq(String title){
        if (StringUtils.hasText(title)){
            return notice.title.contains(title);
        }else {
            return null;
        }
    }
}

/*
    // 정렬 처리 로직을 별도 메소드로 분리
    private void addSorting(JPAQuery<NoticePageForm> query, Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Notice> entityPath = new PathBuilder<>(Notice.class, "notice");
            OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
                case "id" -> new OrderSpecifier<>(order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        entityPath.getNumber(order.getProperty(), Long.class));
                case "title" -> new OrderSpecifier<>(order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        entityPath.getString(order.getProperty()));
                default -> throw new IllegalArgumentException("정렬할 수 없는 속성: " + order.getProperty());
            };
            query.orderBy(orderSpecifier);
        }
    }
}*/