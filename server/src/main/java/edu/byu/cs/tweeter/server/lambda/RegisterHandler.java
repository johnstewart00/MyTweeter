package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class RegisterHandler extends Handler implements RequestHandler<RegisterRequest, RegisterResponse>  {
        @Override
        public RegisterResponse handleRequest(RegisterRequest registerRequest, Context context) {
            UserService userService = injector.getInstance(UserService.class);
            return userService.register(registerRequest);
        }
}

