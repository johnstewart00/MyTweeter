package edu.byu.cs.tweeter.server.dao.accessHelpers;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class UserBean {
    public UserBean(){}
    public UserBean(String alias, String password, String firstName, String lastName, String imageURL){
        this.alias = alias;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
    }
    private String alias;
    private String password;
    private String firstName;
    private String lastName;
    private String imageURL;

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
