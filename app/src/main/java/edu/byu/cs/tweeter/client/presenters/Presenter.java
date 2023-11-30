package edu.byu.cs.tweeter.client.presenters;

public abstract class Presenter {
    public interface view {
        void displayMessage(String message);

    }
}
