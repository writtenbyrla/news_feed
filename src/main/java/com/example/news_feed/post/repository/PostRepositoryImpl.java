package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.Post;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.news_feed.post.domain.QPost.post;


public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    // 제목, 내용 검색
    @Override
    public Page<Post> findByOption(String keyword, Pageable pageable) {
        var query = queryFactory.select(post)
                .from(post)
                .where(containTitle(keyword).or(containContent(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        query.orderBy(post.createdAt.desc());

        var posts = query.fetch();
        long totalSize = queryFactory.select(Wildcard.count)
                .from(post)
                .where(containTitle(keyword).or(containContent(keyword)))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);

    }

    // 작성자 이름으로 게시글 검색
    @Override
    public Page<Post> findByUser(String username, Pageable pageable) {
        var query = queryFactory.select(post)
                .from(post)
                .where(eqUsername(username))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        query.orderBy(post.createdAt.desc());
        var posts = query.fetch();

        long totalSize = queryFactory.select(Wildcard.count)
                .from(post)
                .where(eqUsername(username))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
    }

    // 제목
    private BooleanExpression containTitle(String title) {
        if(title == null || title.isEmpty()){
            return Expressions.asBoolean(true);
        }
        return post.title.containsIgnoreCase(title);
    }

    // 내용
    private BooleanExpression containContent(String content) {
        if(content == null || content.isEmpty()){
            return Expressions.asBoolean(true);
        }
        return post.content.containsIgnoreCase(content);
    }

    // 작성자
    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()){
            return null;
        }
        return post.user.username.equalsIgnoreCase(username);
    }



}
