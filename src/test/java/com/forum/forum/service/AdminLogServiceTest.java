package com.forum.forum.service;

import com.forum.forum.model.AdminLog;
import com.forum.forum.repository.admin.DataJpaAdminLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.forum.forum.AdminLogTestData.*;
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

//    @Test
//    void getAllLogSuccess() {
//        when(logRepository.getAll()).thenReturn(TEST_LOG);
//
//        List<AdminLog> logs = logService.getAll();
//
//        assertEquals(TEST_LOG.size(), logs.size());
//    }
//
//    @Test
//    void searchLogByUsernameSuccess() {
//        List<AdminLog> logs = List.of(LOG_BAN, LOG_UNBAN);
//        when(logRepository.getLogByLogin(USER.getLogin())).thenReturn(List.of(LOG_BAN, LOG_UNBAN));
//
//        List<AdminLog> verifiableLogs = logService.searchByUsername(USER.getLogin());
//
//        assertEquals(logs.size(), verifiableLogs.size());
//
//        verify(logRepository).getLogByLogin(USER.getLogin());
//    }
}