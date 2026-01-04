package com.forum.forum.repository.forum;

import com.forum.forum.readmodel.TopicListView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicListViewRepository extends JpaRepository<TopicListView, Integer> {

    List<TopicListView> findAllByOrderByCreatedAtDesc();

    List<TopicListView> findAllByOrderByCreatedAtAsc();

    List<TopicListView> findAllByOrderByLikesCountDesc();

    List<TopicListView> findByTitleContainingIgnoreCase(String keyword);
}
