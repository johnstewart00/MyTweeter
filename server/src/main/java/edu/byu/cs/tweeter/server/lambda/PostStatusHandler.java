package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler extends Handler implements RequestHandler<PostStatusRequest, PostStatusReponse> {
    @Override
    public PostStatusReponse handleRequest(PostStatusRequest request, Context context) {
        StatusService service = injector.getInstance(StatusService.class);
        return service.postStatus(request);
    }
}
