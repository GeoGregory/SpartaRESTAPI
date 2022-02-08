package com.sparta.api.spartarestapi.exceptions;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(Integer id) {
        super("Could not find Course");
    }
}
