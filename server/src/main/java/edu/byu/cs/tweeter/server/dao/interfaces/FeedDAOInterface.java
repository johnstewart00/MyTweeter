package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface FeedDAOInterface {
    Pair<List<Status>, Boolean> getStatuses(String targetUserAlias, int limit, String lastPost);
}
