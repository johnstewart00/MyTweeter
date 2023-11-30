package edu.byu.cs.tweeter.client.presenters.authenticatedPresenters;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.presenters.Presenter;
import edu.byu.cs.tweeter.client.services.AuthenticateService;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthenticatedPresenter {
    private EditText firstName;
    private EditText lastName;
    private ImageView imageToUpload;
    public RegisterPresenter (View view){
        super(view);
    }
    public void register( byte[] imageBytes) {
        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);
        authService.register(firstName.getText().toString(), lastName.getText().toString(), alias.getText().toString(), password.getText().toString(), imageBytesBase64, new authObserver());
    }
    public void initiateRegistration(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.password = password;
        this.imageToUpload = imageToUpload;
        validateRegistration();
        view.setErrorView(null);
        view.displayInitialToast("Registering...");
    }
    public void validateRegistration() {
        if (firstName.getText().length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.getText().length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.getText().length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
}
