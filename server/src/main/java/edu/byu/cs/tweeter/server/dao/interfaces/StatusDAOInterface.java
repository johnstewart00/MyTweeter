package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;

public interface StatusDAOInterface {
    PostStatusReponse postStatus(PostStatusRequest request);
}
