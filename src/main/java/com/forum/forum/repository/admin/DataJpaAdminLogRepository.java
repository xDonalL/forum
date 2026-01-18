package com.forum.forum.repository.admin;

import com.forum.forum.model.AdminLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataJpaAdminLogRepository {

    private final CrudAdminLogRepository logRepository;

    public AdminLog save(AdminLog adminLog) {
        return logRepository.save(adminLog);
    }

    public Page<AdminLog> getAll(Pageable pageable) {
        return logRepository.findAllByOrderByPerformedAtDesc(pageable);
    }

    public Page<AdminLog> getLogByLogin(Pageable pageable, String username) {
        return logRepository.findByUsernameContainingIgnoreCase(pageable, username);
    }
}
