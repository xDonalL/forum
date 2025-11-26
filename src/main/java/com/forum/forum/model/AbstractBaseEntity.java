package com.forum.forum.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class AbstractBaseEntity {
    private Integer id;

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "[id=" + id + "]";
    }
}
