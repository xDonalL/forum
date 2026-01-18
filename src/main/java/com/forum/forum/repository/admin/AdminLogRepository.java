package com.forum.forum.repository.admin;

import com.forum.forum.model.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLogRepository {

    AdminLog save(AdminLog adminLog);

    Page<AdminLog> getAll(Pageable pageable);

    Page<AdminLog> getLogByLogin(Pageable pageable, String username);
}
