package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends BaseHandler<StatusService.StatusObserver> {

    public GetStoryHandler(StatusService.StatusObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        List<Status> statuses=(List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
        boolean hasMorePages=msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
        observer.displayMoreStory(hasMorePages, statuses);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "get story";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
