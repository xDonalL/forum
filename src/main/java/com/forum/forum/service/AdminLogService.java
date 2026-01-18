package com.forum.forum.service;

import com.forum.forum.model.ActionLog;
import com.forum.forum.model.AdminLog;
import com.forum.forum.repository.admin.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private static final Logger log = LoggerFactory.getLogger(AdminLogService.class);

    private final AdminLogRepository logRepository;

    @CacheEvict(value = "logList", allEntries = true)
    public AdminLog logAction(String username, ActionLog action, String targetLogin, Integer targetId) {
        AdminLog log = new AdminLog(username, action, targetLogin, targetId);
        return logRepository.save(log);
    }

    @Cacheable("logList")
    public Page<AdminLog> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return logRepository.getAll(pageable);
    }

    public Page<AdminLog> searchByUsername(int page, int size, String username) {
        log.debug("Searching log: username='{}'", username);

        Pageable pageable = PageRequest.of(page, size);

        Page<AdminLog> result = logRepository.getLogByLogin(pageable, username);
        checkNotFound(result, "log with username=" + username + " not exist");

        log.info("Search completed: username='{}', found={}", username, result.getTotalElements());
        return result;
    }
}
