package edu.byu.cs.tweeter.server.lambda;

import com.google.inject.AbstractModule;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UploadImageDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UploadImageDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;

public class InjectionModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(FeedDAOInterface.class).to(FeedDAO.class);
        bind(AuthTokenDAOInterface.class).to(AuthTokenDAO.class);
        bind(FollowDAOInterface.class).to(FollowDAO.class);
        bind(StatusDAOInterface.class).to(StatusDAO.class);
        bind(StoryDAOInterface.class).to(StoryDAO.class);
        bind(UploadImageDAOInterface.class).to(UploadImageDAO.class);
        bind(UserDAOInterface.class).to(UserDAO.class);
    }
}
