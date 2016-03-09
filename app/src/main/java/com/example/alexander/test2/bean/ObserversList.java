package com.example.alexander.test2.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 09.03.2016.
 */
public class ObserversList {
    private static List<WebObserver> observers = new ArrayList<>();

    public static void addObserver(WebObserver observer){
        observers.add(observer);
    }

    public static void removeObserver(WebObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObservers() {
        for (WebObserver observer: observers) {
            observer.eventHandler();
        }
    }
}
