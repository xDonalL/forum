package com.forum.forum.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Subselect("""
            select 
                t.id as topic_id,
                t.title as title,
                t.created_at as created_at,
                a.id as author_id,
                a.login as author_login,
                a.avatar as author_avatar,
                count(distinct c.id) as comments_count,
                count(distinct u.user_id) as likes_count
            from topic t
            join users a on t.author_id = a.id
            left join topic_comment c on c.topic_id = t.id
            left join topic_likes u on u.topic_id = t.id
            group by 
                t.id,
                t.title,
                t.created_at,
                a.id,
                a.login,
                a.avatar
        """)
public class TopicListView {

    @Id
    @Column(name = "topic_id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "author_login")
    private String authorLogin;

    @Column(name = "author_avatar")
    private String authorAvatar;

    @Column(name = "comments_count")
    private long commentsCount;

    @Column(name = "likes_count")
    private long likesCount;
}
