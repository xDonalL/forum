package com.forum.forum.service;

import com.forum.forum.model.ActionLog;
import com.forum.forum.model.AdminLog;
import com.forum.forum.repository.admin.DataJpaAdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private static final Logger log = LoggerFactory.getLogger(AdminLogService.class);

    private final DataJpaAdminLogRepository logRepository;

    public AdminLog logAction(String username, ActionLog action, String targetLogin, Integer targetId) {
        AdminLog log = new AdminLog(username, action, targetLogin, targetId);
        return logRepository.save(log);
    }

    public List<AdminLog> getAll() {
        return logRepository.getAll();
    }

    public List<AdminLog> searchByUsername(String username) {
        log.debug("Searching log: username='{}'", username);

        List<AdminLog> result = logRepository.getTopicsByTopicName(username);
        checkNotFound(result, "log with username=" + username + " not exist");

        log.info("Search completed: username='{}', found={}", username, result.size());
        return result;
    }
}
