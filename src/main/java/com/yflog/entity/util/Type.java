package com.yflog.entity.util;

/**
 * Created by vincent on 10/2/16.
 */
public enum Type {
    OUT(1),
    IN(2);
    private int type;
    Type(int tp) {
        type = tp;
    }

    public int getType() {
        return type;
    }
}
