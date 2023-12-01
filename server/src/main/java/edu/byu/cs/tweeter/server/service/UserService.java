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
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UploadImageDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import jakarta.inject.Inject;

public class UserService extends Service {
    private UserDAOInterface userDAO;
    private UploadImageDAOInterface uploadImageDAO;

    @Inject
    public UserService(UserDAOInterface userDAO, UploadImageDAOInterface uploadImageDAO, AuthTokenDAOInterface authTokenDAO){
        super(authTokenDAO);
        setUserDAO(userDAO);
        setUploadImageDAO(uploadImageDAO);
    }

    public LoginResponse login(LoginRequest request) {
        checkRequest(request);
        checkNull(request.getUsername(), NO_USERNAME);
        checkNull(request.getPassword(), NO_PASSWORD);

        Pair<User, AuthToken> result = userDAO.login(request);
        checkNull(result, INVALID_CREDENTIALS);

        return new LoginResponse(result.getFirst(), result.getSecond());
    }
    public RegisterResponse register(RegisterRequest request) {
        checkRequest(request);
        checkNull(request.getUsername(), NO_USERNAME);
        checkNull(request.getPassword(), NO_PASSWORD);


        String imageUrl = uploadImageDAO.uploadImage(request.getImage(), request.getUsername());
        Pair<User, AuthToken> result = userDAO.register(request.getUsername(), request.getPassword(), request.getFirstName(), request.getLastName(), imageUrl );
        return new RegisterResponse(result.getFirst(), result.getSecond());
    }

    public UserResponse getUser(UserRequest request) {
        checkRequest(request);
        checkNull(request.getCurrUser(), NO_CURRENT_USER);
        checkAuthToken(request.getCurrUser().getAlias(), request.getAuthToken());

        User requestedUser = userDAO.getUser(request.getAlias());
        checkNull(requestedUser, String.format("[Bad Request] requested user %s does not exist", request.getAlias()));

        return new UserResponse(requestedUser);
    }

    public LogoutResponse logout(LogoutRequest request) {
        checkRequest(request);
        checkNull(request.getCurrUser(), NO_CURRENT_USER);
        checkAuthToken(request.getCurrUser().getAlias(), request.getAuthToken());

        return userDAO.logout(request);
    }

    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    public UploadImageDAOInterface getUploadImageDAO() {
        return uploadImageDAO;
    }

    public void setUploadImageDAO(UploadImageDAOInterface uploadImageDAO) {
        this.uploadImageDAO = uploadImageDAO;
    }
}
