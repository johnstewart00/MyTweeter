package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UploadImageDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import jakarta.inject.Inject;

public class UserService {
    private AuthTokenDAOInterface authTokenDAO;
    private UserDAOInterface userDAO;

    @Inject
    public UserService(AuthTokenDAOInterface authTokenDAO, UserDAOInterface userDAO){
        setAuthTokenDAO(authTokenDAO);
        setUserDAO(userDAO);
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        Pair<User, AuthToken> result = userDAO.login(request);
        if (result == null) throw new RuntimeException("[Bad Request] Did not find the user");
        return new LoginResponse(result.getFirst(), result.getSecond());
    }
    public RegisterResponse register(RegisterRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        UploadImageDAO uploadImageDAO = new UploadImageDAO();
        String imageUrl = uploadImageDAO.uploadImage(request.getImage(), request.getUsername());
        Pair<User, AuthToken> result = userDAO.register(request.getUsername(), request.getPassword(), request.getFirstName(), request.getLastName(), imageUrl );
        return new RegisterResponse(result.getFirst(), result.getSecond());
    }

    public UserResponse getUser(UserRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if (request.getAuthToken() == null) throw new RuntimeException("[Bad Request] You must include an AuthToken");
        if (request.getCurrUser() == null) throw new RuntimeException("[Bad Request] You have to have a current user");
        if (!authTokenDAO.isValidAuthToken(request.getCurrUser().getAlias(), request.getAuthToken()))
            throw new RuntimeException("[AuthError] unauthenticated request");
        User requestedUser = userDAO.getUser(request.getAlias());
        if (requestedUser == null){
            throw new RuntimeException(String.format("[Bad Request] requested user %s does not exist", request.getAlias()));
        }
        return new UserResponse(requestedUser);
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if (request.getCurrUser() == null) throw new RuntimeException("[Bad Request] you must include currUser");
        if (request.getAuthToken() == null) throw new RuntimeException("[Bad Request] you must include an authToken");
        if (!authTokenDAO.isValidAuthToken(request.getCurrUser().getAlias(), request.getAuthToken()))
            throw new RuntimeException("[AuthError] unauthenticated request");
        return userDAO.logout(request);
    }
}
