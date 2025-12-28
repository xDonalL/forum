package com.forum.forum.repository.admin;

import com.forum.forum.model.AdminLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaAdminLogRepository {

    public DataJpaAdminLogRepository(CrudAdminLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private final CrudAdminLogRepository logRepository;

    public AdminLog save(AdminLog adminLog) {
        return logRepository.save(adminLog);
    }

    public List<AdminLog> getAll() {
        return logRepository.findAllByOrderByPerformedAtDesc();
    }

    public List<AdminLog> getTopicsByTopicName(String username) {
        return logRepository.findByUsernameContainingIgnoreCase(username);
    }
}
