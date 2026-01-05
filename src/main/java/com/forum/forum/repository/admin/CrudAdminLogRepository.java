package com.forum.forum.repository.admin;

import com.forum.forum.model.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudAdminLogRepository extends JpaRepository<AdminLog, Integer> {

    Page<AdminLog> findByUsernameContainingIgnoreCase(Pageable pageable, String keyword);

    Page<AdminLog> findAllByOrderByPerformedAtDesc(Pageable pageable);
}
