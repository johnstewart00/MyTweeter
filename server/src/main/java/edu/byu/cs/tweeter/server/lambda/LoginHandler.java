package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler extends Handler implements RequestHandler<LoginRequest, LoginResponse> {

    public LoginHandler(){
        super();
        System.out.println("called the super to invoke the Handler constructor");
    }
    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        UserService userService = injector.getInstance(UserService.class);
        return userService.login(loginRequest);
    }
}
