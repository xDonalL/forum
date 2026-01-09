package com.forum.forum.service;

import com.forum.forum.model.AdminLog;
import com.forum.forum.repository.admin.DataJpaAdminLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import static com.forum.forum.AdminLogTestData.LOG_DELETE_COMMENT;
import static com.forum.forum.AdminLogTestData.TEST_LOG;
import static com.forum.forum.PageTestData.*;
import static com.forum.forum.UserTestData.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminLogServiceTest {

    @Mock
    private DataJpaAdminLogRepository logRepository;

    @InjectMocks
    private AdminLogService logService;

    @Test
    void logActionAccess() {
        AdminLog newLog = LOG_DELETE_COMMENT;
        when(logRepository.save(any(AdminLog.class))).thenReturn(newLog);

        AdminLog savedLog = logService.logAction(newLog.getUsername(),
                newLog.getAction(), newLog.getTargetLogin(), newLog.getTargetId());

        assertNotNull(savedLog);
        assertEquals(newLog.getTargetLogin(), savedLog.getTargetLogin());
        verify(logRepository).save(any(AdminLog.class));
    }

    @Test
    void getAllLogSuccess() {
        when(logRepository.getAll(PAGE)).thenReturn(TEST_LOG);

        Page<AdminLog> logs = logService.getAll(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(TEST_LOG.getTotalElements(), logs.getTotalElements());
    }

    @Test
    void searchLogByUsernameSuccess() {
        Page<AdminLog> logs = TEST_LOG;
        when(logRepository.getLogByLogin(PAGE, USER.getLogin())).thenReturn(logs);

        Page<AdminLog> verifiableLogs = logService.searchByUsername(PAGE_NUMBER, PAGE_SIZE, USER.getLogin());

        assertEquals(logs.getTotalElements(), verifiableLogs.getTotalElements());

        verify(logRepository).getLogByLogin(PAGE, USER.getLogin());
    }
}