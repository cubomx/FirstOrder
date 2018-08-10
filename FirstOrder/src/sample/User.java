package sample;

public class User {
    private String userName;
    private String userPassword;
    private String type;

    public User(String userName, String userPassword, String type) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
