package Pojos;

/**
 * POJOS: Plain Old Java Object
 * A pojo class must be public and its variables must be private
 * A pojo can have a constructor with arguments, the variables should have getters and setters to access data
 *  Pojo classes are used for creating JSON and XML payloads - DATA for APIs
 */

public class logInPojos {

    public logInPojos(){

    }

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
