package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.model.Topic;
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
                group by
                                t.id,
                                t.title,
                                t.content,
                                t.createdAt,
                                a.id,
                                a.login,
                                a.avatar
            """)
    TopicDto findTopicDetails(@Param("topicId") int topicId);

}

