package info.prateep.android.mymealbag.model;

/**
 * Created by prateep.gedupudi on 2/27/2016.
 */
public class User {
    private String uid;
    private String name;
    private String email;
    private String mobile;

    public User() {
    }

    public User(String uID, String name, String email, String mobile) {
        this.uid = uID;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
