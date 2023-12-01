package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.unFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.unFollowResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private AuthTokenDAOInterface authTokenDAO;
    private FollowDAOInterface followDAO;

    @Inject
    public FollowService(AuthTokenDAOInterface authTokenDAO, FollowDAOInterface followDAO){
        setAuthTokenDAO(authTokenDAO);
        setFollowDAO(followDAO);
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    public FollowDAOInterface getFollowDAO() {
        return followDAO;
    }

    public void setFollowDAO(FollowDAOInterface followDAO) {
        this.followDAO = followDAO;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(!authTokenDAO.isValidAuthToken(request.getFollowerAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        Pair<List<User>, Boolean> pair;
        try {
            pair = followDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        } catch(Exception ex) {
            throw ex;
        }
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(!authTokenDAO.isValidAuthToken(request.getFolloweeAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }

        Pair<List<User>, Boolean> pair = followDAO.getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }


    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if(request == null) throw new RuntimeException("[InternalError] Sorry for the internal error");
        if (request.getFollowee() == null){
            throw new RuntimeException("[Bad Request] you have to provide a followee");
        }
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] you have to provide a authtoken");
        }
        if(!authTokenDAO.isValidAuthToken(request.getFollowee().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        return followDAO.getFollowerCount(request);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if(request == null) throw new RuntimeException("[InternalError] Sorry for the internal error");
        if (request.getFollower() == null){
            throw new RuntimeException("[Bad Request] you have to provide a follower");
        }
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] you have to provide a authtoken");
        }
        if(!authTokenDAO.isValidAuthToken(request.getFollower().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        return followDAO.getFollowingCount(request);
    }

    public FollowResponse follow(FollowRequest request) {
        if(request == null) throw new RuntimeException("[InternalError] Sorry for the internal error");
        if (request.getCurrUser() == null){
            throw new RuntimeException("[Bad Request] you have to provide a currUser");
        }
        if (request.getTargetUser() == null){
            throw new RuntimeException("[Bad Request] you have to provide a targetUser");
        }
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] you have to provide a authtoken");
        }
        if(!authTokenDAO.isValidAuthToken(request.getCurrUser().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        return followDAO.followTask(request);
    }

    public unFollowResponse unfollow(unFollowRequest request) {
        if(request == null) throw new RuntimeException("[InternalError] Sorry for the internal error");
        if (request.getCurrUser() == null) throw new RuntimeException("[Bad Request] You must include a currUser");
        if (request.getTargetUser() == null) throw new RuntimeException("[Bad Request] You must include a target user to remove");
        if (request.getAuthToken() == null) throw new RuntimeException("[Bad Request] You must include an authToken");
        if(!authTokenDAO.isValidAuthToken(request.getCurrUser().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        return followDAO.unfollowTask(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request == null) throw new RuntimeException("[InternalError] Sorry for the internal error");
        if(request.getFollower() == null){
            throw new RuntimeException("[Bad Request] we need to have a follower");
        }
        if(request.getFollowee() == null){
            throw new RuntimeException("[Bad Request] we need to have a followee");
        }
        if(!authTokenDAO.isValidAuthToken(request.getFollowee().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[AuthError] you are not validated");
        }
        return followDAO.isFollower(request);
    }
}
