package com.skeleton.project.utils;

import java.util.Collections;

public class Utils {

    public static <T extends Iterable> T nullGuard(T item) {
        if (item == null) {
            return (T) Collections.EMPTY_LIST;
        } else {
            return item;
        }
    }
}
