package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.news_feed.user.domain.QUser.user;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    // 유저네임으로 유저 검색
    @Override
    public List<User> findByUsername(String username) {
        JPQLQuery<User> query = queryFactory.select(user)
                .from(user)
                .where(eqUsername(username));
        return query.fetch();
    }

    // 유저
    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()){
            return null;
        }
        return user.username.containsIgnoreCase(username);
    }

}
