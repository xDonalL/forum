package com.forum.forum.repository.forum;

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

}

