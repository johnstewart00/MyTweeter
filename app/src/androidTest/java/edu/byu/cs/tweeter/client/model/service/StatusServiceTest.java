package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {
    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        // Setup an observer for the FollowService
        observer = new StatusServiceObserver();

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    public class StatusServiceObserver implements StatusService.StatusObserver{
        public List<Status> getStatuses() {
            return statuses;
        }

        public void setStatuses(List<Status> statuses) {
            this.statuses = statuses;
        }

        private List<Status>statuses;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        private boolean success;

        @Override
        public void displayMessage(String message) {
            countDownLatch.countDown();
        }

        @Override
        public void displayException(Exception ex) {
            countDownLatch.countDown();
        }

        @Override
        public void logMessage(Exception ex) {
            countDownLatch.countDown();
        }

        @Override
        public void postingToastCancel() {
            countDownLatch.countDown();
        }

        @Override
        public void loadItemsSuccess(List<Status> statuses, boolean hasMorePages) {
            this.statuses = statuses;
            countDownLatch.countDown();
        }

        @Override
        public void displayMoreStory(boolean hasMorePages, List<Status> statuses) {
            this.statuses = statuses;
            this.success = true;
            countDownLatch.countDown();
        }
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getMoreStories(currentAuthToken, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getFakeStatuses().subList(0, 3);
        Assertions.assertEquals(expectedStatuses, observer.getStatuses());
        Assertions.assertTrue(observer.isSuccess());
    }

}
