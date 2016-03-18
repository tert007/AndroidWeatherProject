package com.example.alexander.test2.controller;

/**
 * Created by Alexander on 11.03.2016.
 */
public class CommandException extends Exception {
    public CommandException() {
        super();
    }

    public CommandException(String detailMessage) {
        super(detailMessage);
    }

    public CommandException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommandException(Throwable throwable) {
        super(throwable);
    }
}
