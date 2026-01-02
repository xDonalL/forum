package com.forum.forum.repository.forum;

import com.forum.forum.readmodel.TopicListView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicListViewRepository extends JpaRepository<TopicListView, Integer> {

    List<TopicListView> findAllByOrderByCreatedAtDesc();

    List<TopicListView> findAllByOrderByCreatedAtAsc();

    List<TopicListView> findAllByOrderByLikesCountAsc();

    List<TopicListView> findByTitleContainingIgnoreCase(String keyword);
}
