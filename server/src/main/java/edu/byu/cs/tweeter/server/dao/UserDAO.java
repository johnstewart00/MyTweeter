package edu.byu.cs.tweeter.server.dao;

import com.google.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.accessHelpers.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class UserDAO extends ParentDAO implements UserDAOInterface {
    private static final String TableName = "user";
    private AuthTokenDAOInterface authTokenDAO;
    BCryptPasswordEncoder encoder;
    @Inject
    public UserDAO(AuthTokenDAOInterface authTokenDAO){
        super();
        setAuthTokenDAO(authTokenDAO);
        table = enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));
        encoder = new BCryptPasswordEncoder();
    }
    @Override
    public Pair<User, AuthToken> register(String alias, String password, String firstName,
                                          String lastName, String imageURL) {
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        UserBean userBean = new UserBean(alias, SaltPassword(password), firstName, lastName, imageURL);
        AuthToken authToken = authTokenDAO.setNewAuthToken(alias);
        User user = new User(firstName, lastName, alias, imageURL);
        table.putItem(userBean);
        return new Pair<>(user, authToken);
    }
    @Override
    public Pair<User, AuthToken> login(LoginRequest request) {
        UserBean user = getUserBean(request.getUsername());
        if (user == null) return null;
        if (!Matches(request.getPassword(), user.getPassword()))  throw new RuntimeException("[Bad Request] Incorrect Credentials");
        AuthToken auth = authTokenDAO.setNewAuthToken(request.getUsername());
        User userToReturn = new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImageURL());
        return new Pair<>(userToReturn, auth);
    }
    @Override
    public User getUser(String alias) {
        Key key=Key.builder()
                .partitionValue(alias)
                .build();
        UserBean user = (UserBean) table.getItem(key);
        return new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImageURL());
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) {
        boolean success = authTokenDAO.removeAuthToken(request.getAuthToken());
        return new LogoutResponse(success);
    }

    public UserBean getUserBean(String alias) {
        Key key=Key.builder()
                .partitionValue(alias)
                .build();
        UserBean user = (UserBean) table.getItem(key);
        return user;
    }

    public String SaltPassword(String currPassword){
        String hash = encoder.encode(currPassword);
        return hash;
    }
    public Boolean Matches(String pw, String hash){
        Boolean match = encoder.matches(pw, hash);
        return match;
    }
    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }
}
