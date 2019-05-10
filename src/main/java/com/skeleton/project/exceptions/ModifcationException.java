package com.skeleton.project.exceptions;

public class ModifcationException extends RuntimeException{

    public ModifcationException(String userIdentifier, String groupId, String groupName) {
        super("User " + userIdentifier + " request can not modify group " + groupId +  " " + groupName);
    }
}
