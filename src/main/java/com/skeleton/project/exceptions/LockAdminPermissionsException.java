package com.skeleton.project.exceptions;

public class LockAdminPermissionsException extends RuntimeException {

    public LockAdminPermissionsException(String lockId, String userIdentifier) {
        super("User " + userIdentifier + " does not have admin permissions for lock " + lockId);
    }
}
