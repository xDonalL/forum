package com.forum.forum.repository.admin;

import com.forum.forum.model.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrudAdminLogRepository extends JpaRepository<AdminLog, Integer> {

    List<AdminLog> findByUsernameContainingIgnoreCase(String keyword);

    List<AdminLog> findAllByOrderByPerformedAtDesc();
}
