package com.forum.forum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AdminLog extends AbstractBaseEntity {

    public AdminLog(String username, ActionLog action, String targetLogin, Integer targetId) {
        this.username = username;
        this.action = action;
        this.targetLogin = targetLogin;
        this.targetId = targetId;
    }

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, updatable = false)
    private ActionLog action;

    @Column(name = "target_login", nullable = false, updatable = false)
    private String targetLogin;

    @Column(name = "target_id", nullable = false, updatable = false)
    private Integer targetId;

    @Column(name = "performedAt", nullable = false, updatable = false)
    private final LocalDateTime performedAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "AdminLog{" +
                "username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", targetId='" + targetId + '\'' +
                ", targetLogin='" + targetLogin + '\'' +
                ", performedAt=" + performedAt +
                '}';
    }
}
