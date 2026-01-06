package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicCommentDto;
import com.forum.forum.model.TopicComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CrudTopicCommentRepository extends JpaRepository<TopicComment, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM TopicComment c WHERE c.id=:id")
    int delete(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM TopicComment c WHERE c.topic.id = :topicId")
    int deleteByTopicId(@Param("topicId") int topicId);

    @Query("""
                select new com.forum.forum.dto.TopicCommentDto(
                    c.id,
                    c.comment,
                    c.dateCreated,
                    c.updatedAt,
                    a.id,
                    a.login,
                    a.avatar,
                    count(distinct u.id),
                             case
                            when count(distinct mu.id) > 0 then true
                            else false
                        end
                )
                from TopicComment c
                join c.author a
                left join c.likedUsers u
                left join c.likedUsers mu on mu.id = :userId
                where c.topic.id = :topicId
                group by
                        c.id,
                        c.comment,
                        c.dateCreated,
                        c.updatedAt,
                        a.id,
                        a.login,
                        a.avatar
                order by c.dateCreated asc
            """)
    Page<TopicCommentDto> findCommentsByTopicId(Pageable pageable,
                                                @Param("topicId") int topicId,
                                                @Param("userId") int userId);
}

