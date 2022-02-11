package com.sparta.api.spartarestapi.exceptions;

import javax.xml.bind.ValidationException;

public class InvalidApiKeyException extends ValidationException {
    public InvalidApiKeyException(String message) {
        super(message);
    }
}
