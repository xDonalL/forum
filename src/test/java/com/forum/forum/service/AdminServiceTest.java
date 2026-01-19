package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.forum.forum.PageTestData.*;
import static com.forum.forum.UserTestData.*;
import static com.forum.forum.model.Role.MODERATOR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getFilterNullUserSuccess() {
        Page<User> allPage = new PageImpl<>(List.of(USER, ADMIN, MODER));

        when(userRepository.getAll(PAGE)).thenReturn(allPage);

        Page<User> verifyAllPage = adminService.filterUsers(PAGE_NUMBER, PAGE_SIZE, null);

        assertEquals(allPage.getTotalElements(), verifyAllPage.getTotalElements());
    }

    @Test
    void getFilterBannedUserSuccess() {
        User userBan = new User(USER);
        userBan.setEnabled(false);
        Page<User> banPage = new PageImpl<>(List.of(userBan));

        when(userRepository.getBanned(PAGE)).thenReturn(banPage);

        Page<User> verifyBanPage = adminService.filterUsers(PAGE_NUMBER, PAGE_SIZE, "banned");

        assertEquals(banPage.getTotalElements(), verifyBanPage.getTotalElements());
    }

    @Test
    void getFilterModerUserSuccess() {
        Page<User> moderPage = new PageImpl<>(List.of(MODER));

        when(userRepository.getByRole(PAGE, "moderator")).thenReturn(moderPage);

        Page<User> verifyModerPage = adminService.filterUsers(PAGE_NUMBER, PAGE_SIZE, "moderator");

        assertEquals(moderPage.getTotalElements(), verifyModerPage.getTotalElements());
    }

    @Test
    void getFilterAdminUserSuccess() {
        Page<User> adminPage = new PageImpl<>(List.of(ADMIN));

        when(userRepository.getByRole(PAGE, "admin")).thenReturn(adminPage);

        Page<User> verifyAdminPage = adminService.filterUsers(PAGE_NUMBER, PAGE_SIZE, "admin");

        assertEquals(adminPage.getTotalElements(), verifyAdminPage.getTotalElements());
    }

    @Test
    void searchUserByLoginSuccess() {
        String login = "login";
        USER.setLogin(login);
        Page<User> adminPage = new PageImpl<>(List.of(USER));

        when(userRepository.getByContainingLogin(PAGE, login)).thenReturn(adminPage);

        Page<User> verifySearchLogin = adminService.search(PAGE_NUMBER, PAGE_SIZE, login, login);

        assertEquals(adminPage.getTotalElements(), verifySearchLogin.getTotalElements());
    }

    @Test
    void searchUserByEmailSuccess() {
        String email = "email";
        USER.setLogin(email);
        Page<User> adminPage = new PageImpl<>(List.of(USER));

        when(userRepository.getByContainingEmail(PAGE, email)).thenReturn(adminPage);

        Page<User> verifySearchEmail = adminService.search(PAGE_NUMBER, PAGE_SIZE, email, email);

        assertEquals(adminPage.getTotalElements(), verifySearchEmail.getTotalElements());
    }

    @Test
    void banUserSuccess() {
        when(userRepository.get(USER_ID)).thenReturn(USER);
        adminService.banUser(USER_ID);
        assertFalse(USER.isEnabled());
    }

    @Test
    void unbanUserSuccess() {
        when(userRepository.get(USER_ID)).thenReturn(USER);
        adminService.unbanUser(USER_ID);
        assertTrue(USER.isEnabled());
    }

    @Test
    void setModeratorSuccess() {
        when(userRepository.get(USER_ID)).thenReturn(USER);
        adminService.setModerator(USER_ID);
        assertTrue(USER.getRoles().contains(MODERATOR));
    }

    @Test
    void removeModeratorSuccess() {
        when(userRepository.get(USER_ID)).thenReturn(USER);
        adminService.removeModerator(USER_ID);
        assertFalse(USER.getRoles().contains(MODERATOR));
    }
}