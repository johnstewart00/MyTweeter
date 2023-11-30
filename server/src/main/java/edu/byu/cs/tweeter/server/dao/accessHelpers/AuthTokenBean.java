package edu.byu.cs.tweeter.server.dao.accessHelpers;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class AuthTokenBean {
    private String alias;

    private long expirationdate;
    private String authtoken;

    public AuthTokenBean(){}
    public AuthTokenBean(AuthToken auth, String alias){
        this.authtoken = auth.getToken();
        this.expirationdate = auth.getTimestamp();
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbPartitionKey
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
    @DynamoDbSortKey
    public long getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(long expirationdate) {
        this.expirationdate = expirationdate;
    }
}
