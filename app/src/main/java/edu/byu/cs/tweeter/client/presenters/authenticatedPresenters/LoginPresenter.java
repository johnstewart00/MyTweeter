package edu.byu.cs.tweeter.client.presenters.authenticatedPresenters;

import android.widget.EditText;

public class LoginPresenter extends AuthenticatedPresenter {

    public LoginPresenter(View view) {
        super(view);
    }
    public void login(EditText alias, EditText password) {
        this.alias = alias;
        this.password = password;
        try {
            validateLogin();
            view.setErrorView(null);
            view.displayInitialToast("Logging In...");
            authService.login(alias.getText().toString(),password.getText().toString(), new authObserver());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            view.setErrorView(e.getMessage());
        }
    }
    public void validateLogin() {
        if (alias.getText().length() > 0 && alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
}
