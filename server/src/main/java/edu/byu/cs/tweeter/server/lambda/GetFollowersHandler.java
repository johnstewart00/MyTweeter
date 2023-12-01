package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler extends Handler implements RequestHandler<FollowersRequest, FollowersResponse>{
    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context)
    {
        FollowService service = injector.getInstance(FollowService.class);
        return service.getFollowers(request);
    }
}
