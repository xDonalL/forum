package com.forum.forum.service;

import com.forum.forum.dto.TopicCommentDto;
import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPageDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicSort;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class TopicService {

    private static final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final DataJpaTopicRepository topicRepository;
    private final DataJpaTopicCommentRepository commentRepository;
    private final UserService userService;
    private final TopicCommentService commentService;
    private final SafeMarkdownService markdownService;

    @PreAuthorize("isAuthenticated()")
    @CacheEvict(value = "topicsPage", allEntries = true)
    @Transactional
    public Topic create(String title, String content, User user) {
        log.debug("Creating topic: title='{}', authorId={}", title, user.getId());

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(user);

        Topic saved = topicRepository.save(topic);

        log.info("Topic created: topicId={}, authorId={}", saved.getId(), user.getId());
        return saved;
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @Transactional
    public Topic update(Integer id, String title, String content) {
        log.debug("Updating topic: id={}", id);

        Topic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id=" + id + " not exist");

        topic.setTitle(title);
        topic.setContent(content);

        Topic updated = topicRepository.save(topic);

        log.info("Topic updated: topicId={}", id);
        return updated;
    }

    public Topic get(Integer id) {
        log.debug("Getting topic: id={}", id);
        return checkNotFound(topicRepository.get(id),
                "topic with id=" + id + " not exist");
    }

    public TopicPageDto getTopicView(int page, int size, Integer id) {
        log.debug("Getting topicDto: id={}", id);

        Integer currentUserId = userService.getCurrentUser().getId();

        Pageable pageable = PageRequest.of(page, size);
        TopicDto topicDto = htmlTopicRender(topicRepository.getDetails(id, currentUserId));
        Page<TopicCommentDto> commentDto = commentService.getPageForTopic(pageable, id, currentUserId);

        checkNotFound(topicDto, "topic with id=" + id + " not exist");
        return new TopicPageDto(topicDto, commentDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @CacheEvict(value = "topicsPage", allEntries = true)
    @Transactional
    public boolean delete(Integer id) {
        log.debug("Deleting topic: id={}", id);

        Topic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id=" + id + " not exist");

        commentRepository.deleteByTopicId(id);
        boolean deleted = topicRepository.delete(id);

        log.info("Topic deleted: topicId={}", id);
        return deleted;
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean toggleLike(int topicId) {
        User currentUser = userService.getCurrentUser();
        log.debug("Toggle like to topic: topicId={}, userId={}",
                topicId, currentUser.getId());

        Topic topic = topicRepository.get(topicId);

        if (topic.getLikedUsers().contains(currentUser)) {
            log.info("Topic like removed: topicId={}, userId={}",
                    topicId, currentUser.getId());
            return topic.getLikedUsers().remove(currentUser);
        } else {
            log.info("Topic like added: topicId={}, userId={}",
                    topicId, currentUser.getId());
            return topic.getLikedUsers().add(currentUser);
        }
    }

    @Cacheable("topicsPage")
    public Page<TopicPagesDto> getAllSorted(int page, int size, TopicSort sort) {
        log.debug("Getting topics sorted by '{}'", sort);

        Pageable pageable = PageRequest.of(page, size);

        if (sort == null) {
            return topicRepository.getAllTopics(pageable);
        }

        return switch (sort) {
            case TopicSort.LIKES_DESC -> topicRepository.getTopicsSortByLikes(pageable);
            case TopicSort.DATE_ASC -> topicRepository.getAllTopicsAsc(pageable);
            default -> topicRepository.getAllTopics(pageable);
        };
    }

    public Page<TopicPagesDto> search(int page, int size, String q) {
        log.debug("Searching topics: query='{}'", q);

        Pageable pageable = PageRequest.of(page, size);

        Page<TopicPagesDto> result = topicRepository.getTopicsByTitle(q, pageable);
        checkNotFound(result, "topic with q=" + q + " not exist");

        log.info("Search completed: query='{}'", q);
        return result;
    }

    private TopicDto htmlTopicRender(TopicDto topic) {
        return new TopicDto(
                topic.id(),
                topic.title(),
                markdownService.toSafeHtml(topic.content()),
                topic.createdAt(),
                topic.updatedAt(),
                topic.authorId(),
                topic.authorLogin(),
                topic.authorAvatar(),
                topic.likesCount(),
                topic.likedByMe());
    }
}