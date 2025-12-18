package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "forum_topics")
public class Topic extends AbstractBaseEntity {

    public Topic(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Topic(Integer id, String title, String content, User author) {
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
    private final List<TopicComment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "topic_likes",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private final Set<User> likedUsers = new HashSet<>();
}
