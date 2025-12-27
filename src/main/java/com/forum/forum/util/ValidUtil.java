package com.forum.forum.util;

import com.forum.forum.model.AbstractBaseEntity;
import com.forum.forum.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidUtil {

    public static final Logger log = LoggerFactory.getLogger(ValidUtil.class);

    public static void checkNotFound(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static boolean checkNotFound(boolean found, String msg) {
        if (!found) {
            log.warn("Entity not found: {}", msg);
            throw new NotFoundException("Not found entity with " + msg);
        }
        return true;
    }

    public static void checkIsNew(AbstractBaseEntity bean) {
        if (!bean.isNew()) {
            log.warn("Entity must be new (id=null), but was id={}",
                    bean.getId());
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }
}
