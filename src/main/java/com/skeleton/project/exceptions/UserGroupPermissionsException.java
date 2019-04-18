package com.skeleton.project.exceptions;

public class UserGroupPermissionsException extends RuntimeException {

    public UserGroupPermissionsException(String userIdentifier, String groupId, String groupName) {
        super("User " + userIdentifier + " does not have admin permissions for group " + groupId +  " " + groupName);
    }
}