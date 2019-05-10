package com.skeleton.project.exceptions;

public class UserGroupAdminPermissionsException extends RuntimeException {

    public UserGroupAdminPermissionsException(String userIdentifier, String groupId, String groupName) {
        super("User " + userIdentifier + " does not have admin permissions for group " + groupId +  " " + groupName);
    }
}