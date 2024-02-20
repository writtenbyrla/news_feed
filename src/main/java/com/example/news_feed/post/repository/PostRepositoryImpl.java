package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.news_feed.post.domain.QPost.post;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    // 이름, 내용, 유저네임으로 검색
    @Override
    public List<Post> findBySearchOption(String title, String content, String username) {
        JPQLQuery<Post> query = queryFactory.select(post)
                .from(post)
                        .where(containTitle(title).or(containContent(content)).or((eqUsername(username))));
        return query.fetch();
    }

    private BooleanExpression containTitle(String title) {
        if(title == null || title.isEmpty()){
            return null;
        }
        return post.title.containsIgnoreCase(title);
    }

    private BooleanExpression containContent(String content) {
        if(content == null || content.isEmpty()){
            return null;
        }
        return post.content.containsIgnoreCase(content);
    }

    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()){
            return null;
        }
        return post.user.username.equalsIgnoreCase(username);
    }

}
