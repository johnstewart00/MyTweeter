package edu.byu.cs.tweeter.client.model.net;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeIntegrationTest<T> {
    private static final String SERVER_URL = "https://te3poa91ti.execute-api.us-east-2.amazonaws.com/development";
    private ServerFacade serverFacade;

    @BeforeEach
    public void setup(){
        serverFacade = new ServerFacade();
    }

    @Test
    public void registerIntegrationTest(){

        RegisterRequest registerRequest = new RegisterRequest("steve", "test","JOhn","stew","image");
        RegisterResponse response = null;
        try {
            response = serverFacade.register(registerRequest, "register");
        } catch (IOException e) {
            fail("should not be an error");
        } catch (TweeterRemoteException e) {
            fail("should not be an error");
        }
        assertNotNull(response);
        assertNotNull(response.getAuth());
        assertNotNull(response.getRegisteredUser());
        assertTrue(response.isSuccess());
    }
    @Test
    public void getFollowersIntegrationTest(){
        FollowersResponse response = null;
        FollowersRequest request = new FollowersRequest(new AuthToken("string", 3456), "string", 56, "string");
        try {
            response = serverFacade.getFollowers(request, "/getfollowers");
        } catch (IOException e) {
            fail("should not be an error");
        } catch (TweeterRemoteException e) {
            fail("should not be an error");
        }
        assertNotNull(response);
        assertNotNull(response.getFollowers());
        assertTrue(response.isSuccess());
    }

    @Test
    public void getFollowingCountIntegrationTest(){
        FollowingCountResponse response = null;
        FollowingCountRequest request = new FollowingCountRequest(new User("john", "locks", "@ere", "image"), new AuthToken("something", 12345));
        try {
            response = serverFacade.getFollowingCount(request, "/followingcount");
        } catch (IOException e) {
            fail("should not be an error");
        } catch (TweeterRemoteException e) {
            fail("should not be an error");
        }
        assertNotNull(response);
        assertNotNull(response.getCount());
        assertTrue(response.isSuccess());
    }
}
