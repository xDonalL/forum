package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "forum_comment")
public class ForumComment extends AbstractBaseEntity {

    public ForumComment(String comment, User author, ForumTopic topic) {
        this.comment = comment;
        this.author = author;
        this.topic = topic;
    }

    public ForumComment(Integer id, String comment, User author, ForumTopic topic) {
        this(comment, author, topic);
        this.id = id;
    }

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date_created", nullable = false, updatable = false)
    private final LocalDateTime dateCreated = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private ForumTopic topic;
}
