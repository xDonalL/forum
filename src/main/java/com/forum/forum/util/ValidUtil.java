package com.forum.forum.util;

import com.forum.forum.util.exception.NotFoundException;
import com.forum.forum.model.AbstractBaseEntity;

public class ValidUtil {
    public static void checkNotFound(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static boolean checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
        return true;
    }

    public static void checkIsNew(AbstractBaseEntity bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }
}
