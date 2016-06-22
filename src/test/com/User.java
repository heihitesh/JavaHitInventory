package test.com;

/**
 * Created by Nilesh Verma on 6/18/2016.
 */
public class User {
    String userName ="---";
    String name ="---";
    String phoneNo = "---" ;
    String accessParam ;
    String firstname;

    public void setUserName(String userName) {
        this.userName = userName;

    }

    public void setName(String name) {
        this.name = name;

    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAccessParam(String accessParam) {
        this.accessParam = accessParam;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAccessParam() {
        return accessParam;
    }



    public void setUserInformation(String firstName) {
        this.firstname = firstName;
    }

    public String getUserInformation() {
        return firstname;
    }
}
