package edu.byu.cs.tweeter.server.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.accessHelpers.AuthTokenBean;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class AuthTokenDAO extends ParentDAO {
    private static final String TableName = "authtoken";
    public AuthTokenDAO(){
        super();
        table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenBean.class));
    }

    public boolean isValidAuthToken(String alias, AuthToken auth){
        Key key=Key.builder()
                .partitionValue(auth.getToken()).sortValue(auth.getTimestamp())
                .build();
        // getting an item with a primary key
        AuthTokenBean authTokenBean = (AuthTokenBean) table.getItem(key);
        return authTokenBean != null;
    }
    public AuthToken setNewAuthToken(String alias){
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        BigInteger timestamp = BigInteger.valueOf(new Long(Timestamp.valueOf(String.valueOf(new Timestamp(System.currentTimeMillis()))).getTime()));
        AuthToken auth = new AuthToken(uuidAsString, timestamp.longValue());
        AuthTokenBean authTokenBean = new AuthTokenBean(auth, alias);
        table.putItem(authTokenBean);
        return auth;
    }
}
