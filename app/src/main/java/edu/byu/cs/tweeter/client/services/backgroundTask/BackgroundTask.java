package edu.byu.cs.tweeter.client.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//parent class for all background tasks
public abstract  class BackgroundTask implements Runnable{
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    private static final String LOG_TAG = "BackgroundTask";
    /**
     * Message handler that will receive task results.
     */
    protected Handler messageHandler;
    protected abstract void loadSuccessBundle(Bundle msgBundle );
    protected abstract void doTask( );

    public BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }
    @Override
    public void run() {
        try {
            doTask();
            sendSuccessMessage();
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private void sendSuccessMessage() {
        Bundle msgBundle=createBundle(true);

        loadSuccessBundle(msgBundle);

        sendMessage(msgBundle);
    }

    private void sendFailedMessage(String message) {
        Bundle msgBundle=createBundle(false);
        msgBundle.putString(MESSAGE_KEY, message);

        sendMessage(msgBundle);
    }

    private void sendExceptionMessage(Exception exception) {
        Bundle msgBundle=createBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        sendMessage(msgBundle);
    }
    @NotNull
    private static Bundle createBundle(boolean value) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, value);
        return msgBundle;
    }
    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected FakeData getFakeData( ) {
        return FakeData.getInstance();
    }
}
