package com.skeleton.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof EntityNotFoundException) {
            return new ResponseEntity<>("Requested id not found", HttpStatus.NOT_FOUND);
        }

        if (ex instanceof UserGroupPermissionsException) {
            return new ResponseEntity<>("Requesting user does not have admin privileges for group", HttpStatus.NOT_FOUND);
        }

        if (ex instanceof LockAdminPermissionsException) {
            return new ResponseEntity<>("The attempting group owner does not have access to that lock", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
