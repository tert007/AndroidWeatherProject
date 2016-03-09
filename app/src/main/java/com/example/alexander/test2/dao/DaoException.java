package com.example.alexander.test2.dao;

/**
 * Created by Alexander on 09.03.2016.
 */
public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String detailMessage) {
        super(detailMessage);
    }

    public DaoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
