package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "forum_topics")
public class ForumTopic extends AbstractBaseEntity {

    public ForumTopic(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public ForumTopic(Integer id, String title, String content, User author) {
        this(title, content, author);
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "date_created", nullable = false, updatable = false)
    private final LocalDateTime dateCreated = LocalDateTime.now();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ForumComment> comments = new ArrayList<>();
}
