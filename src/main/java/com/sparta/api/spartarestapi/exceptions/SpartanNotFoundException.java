package com.sparta.api.spartarestapi.exceptions;

public class SpartanNotFoundException extends RuntimeException{
    public SpartanNotFoundException(String id) {
        super("Could not find Spartan");
    }

}
