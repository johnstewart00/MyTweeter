package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFeedTask;
import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends BaseHandler<StatusService.StatusObserver> {

    public GetFeedHandler(StatusService.StatusObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        List<Status> statuses=(List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
        boolean hasMorePages=msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
        observer.loadItemsSuccess(statuses, hasMorePages);
    }
    @Override
    public String getTaskName(){
        return "get feed";
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
