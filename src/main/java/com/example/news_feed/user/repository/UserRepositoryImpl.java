package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.news_feed.post.domain.QPost.post;
import static com.example.news_feed.user.domain.QUser.user;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    // 유저네임으로 유저 검색
    @Override
    public Page<User> findByUsername(String username, Pageable pageable) {
        var query = queryFactory.select(user)
                .from(user)
                .where(containUsername(username))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        query.orderBy(user.createdAt.desc());
        var users = query.fetch();

        long totalSize = queryFactory.select(Wildcard.count)
                .from(post)
                .where(containUsername(username))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(users, pageable, () -> totalSize);

    }

    // 유저
    private BooleanExpression containUsername(String username) {
        if(username == null || username.isEmpty()){
            return null;
        }
        return user.username.containsIgnoreCase(username).and(user.status.eq("Y"));
    }

}
