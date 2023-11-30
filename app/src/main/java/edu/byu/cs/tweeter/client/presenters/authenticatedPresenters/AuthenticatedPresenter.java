package edu.byu.cs.tweeter.client.presenters.authenticatedPresenters;

import android.widget.EditText;



import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenters.Presenter;
import edu.byu.cs.tweeter.client.services.AuthenticateService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatedPresenter extends Presenter {
    protected EditText alias;
    protected EditText password;
    AuthenticateService authService;
    protected View view;

    public AuthenticatedPresenter(View view) {
        this.view = view;
        authService = new AuthenticateService();
    }

    public interface View extends view {

        void setErrorView(String message);

        void displayInitialToast(String message);

        void authenticateSuccess(User user);
    }



    protected class authObserver implements AuthenticateService.AuthObserver {
        @Override
        public void loginSuccess(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.authenticateSuccess(loggedInUser);
        }

        @Override
        public void registerSuccess(User registeredUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.authenticateSuccess(registeredUser);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage(ex.getMessage());
        }
    }
}
