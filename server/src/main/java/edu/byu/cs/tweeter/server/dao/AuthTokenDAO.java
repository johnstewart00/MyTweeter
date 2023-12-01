package edu.byu.cs.tweeter.server.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.accessHelpers.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class AuthTokenDAO extends ParentDAO implements AuthTokenDAOInterface {
    private static final String TableName = "authtoken";

    private static final long EXPIRATION_TIME = 50 * 60 * 60 * 1000;
    public AuthTokenDAO(){
        super();
        table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenBean.class));
    }
    @Override
    public boolean isValidAuthToken(String alias, AuthToken auth){
        if (alias == null || auth == null) return false;
        Key key=Key.builder()
                .partitionValue(auth.getToken()).sortValue(auth.getTimestamp())
                .build();
        // getting an item with a primary key
        AuthTokenBean authTokenBean = (AuthTokenBean) table.getItem(key);
        if (authTokenBean != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - auth.getTimestamp() < EXPIRATION_TIME){
                return true;
            } else throw new RuntimeException("Your timestamp is expired");
        }
        return false;
    }
    @Override
    public AuthToken setNewAuthToken(String alias){
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        BigInteger timestamp = BigInteger.valueOf(new Long(Timestamp.valueOf(String.valueOf(new Timestamp(System.currentTimeMillis()))).getTime()));
        AuthToken auth = new AuthToken(uuidAsString, timestamp.longValue());
        AuthTokenBean authTokenBean = new AuthTokenBean(auth, alias);
        table.putItem(authTokenBean);
        return auth;
    }

    @Override
    public boolean removeAuthToken(AuthToken auth) {
        Key key=Key.builder()
                .partitionValue(auth.getToken()).sortValue(auth.getTimestamp())
                .build();
        table.deleteItem(key);
        return true;
    }
}
