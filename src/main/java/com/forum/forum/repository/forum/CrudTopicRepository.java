package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPageDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CrudTopicRepository extends JpaRepository<Topic, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Topic t WHERE t.id=:id")
    int delete(@Param("id") int id);

    @Query("""
                select new com.forum.forum.dto.TopicDto(
                    t.id,
                    t.title,
                    t.content,
                    t.createdAt,
                    a.id,
                    a.login,
                    a.avatar,
                    count(distinct u.id)
                )
                from Topic t
                join t.author a
                left join t.likedUsers u
                where t.id = :topicId
                group by t.id, t.title, t.content, t.createdAt, a.id, a.login, a.avatar
            """)
    TopicDto findTopicDetails(@Param("topicId") int topicId);

    @Query("""
                select new com.forum.forum.dto.TopicPagesDto(
                    t.id,
                    t.title,
                    t.createdAt,
                    a.id,
                    a.login,
                    a.avatar,
                    count(distinct c.id),
                    count(distinct u.id)
                )
                from Topic t
                join t.author a
                left join t.comments c
                left join t.likedUsers u
                group by t.id, t.title, t.createdAt, a.id, a.login, a.avatar
            """)
    Page<TopicPagesDto> findAllForPage(Pageable pageable);

    @Query("""
                select new com.forum.forum.dto.TopicPagesDto(
                    t.id,
                    t.title,
                    t.createdAt,
                    a.id,
                    a.login,
                    a.avatar,
                    count(distinct c.id),
                    count(distinct u.id)
                )
                from Topic t
                join t.author a
                left join t.comments c
                left join t.likedUsers u
                where lower(t.title) like lower(concat('%', :q, '%'))
                group by t.id, t.title, t.createdAt, a.id, a.login, a.avatar
            """)
    Page<TopicPagesDto> findByTitleForPage(String q, Pageable pageable);

    @Query("""
    select new com.forum.forum.dto.TopicPagesDto(
        t.id,
        t.title,
        t.createdAt,
        a.id,
        a.login,
        a.avatar,
        count(distinct c.id),
        count(distinct u.id)
    )
    from Topic t
    join t.author a
    left join t.comments c
    left join t.likedUsers u
    group by t.id, t.title, t.createdAt, a.id, a.login, a.avatar
    order by count(distinct u.id) desc
""")
    Page<TopicPagesDto> findAllOrderByLikes(Pageable pageable);
}

