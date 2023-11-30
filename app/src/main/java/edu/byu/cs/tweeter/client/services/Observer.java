package edu.byu.cs.tweeter.client.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Observer<T> {
    public interface observer {
        void displayMessage(String message);
        void displayException(Exception ex);
    }
    protected void execute(T backgroundTask) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute((Runnable) backgroundTask);
    }
}
