package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler extends Handler implements RequestHandler<UserRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        UserService service = injector.getInstance(UserService.class);
        return service.getUser(request);
    }

    public abstract static class Handler {
        protected Injector injector;

        public Handler(){
            injector = Guice.createInjector(new InjectionModule());
        }
    }
}
