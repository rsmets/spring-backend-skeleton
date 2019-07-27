package com.skeleton.project.enums;

public enum Roles {
    USER(0),
    ADMIN(1),
    OWNER(2);

    private int value;

    private Roles(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }



}
