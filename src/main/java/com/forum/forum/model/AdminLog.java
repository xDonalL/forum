package com.forum.forum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AdminLog extends AbstractBaseEntity {

    public AdminLog(String username, String action, String targetLogin, String targetId) {
        this.username = username;
        this.action = action;
        this.targetLogin = targetLogin;
        this.targetId = targetId;
    }

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Column(name = "action", nullable = false, updatable = false)
    private String action;

    @Column(name = "target_login", nullable = false, updatable = false)
    private String targetLogin;

    @Column(name = "target_id", nullable = false, updatable = false)
    private String targetId;

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
