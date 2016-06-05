package info.prateep.android.mymealbag.model;

import java.util.List;
import java.util.Map;

/**
 * Created by prateep.gedupudi on 2/27/2016.
 */
public class User {
    private String uid;
    private String name;
    private String email;
    private String mobile;
    private String isAdmin;
    private Map<String,List<String>> items;


    public User() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setItems(Map<String, List<String>> items) {
        this.items = items;
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

    public String getIsAdmin() {
        return isAdmin;
    }

    public Map<String,List<String>> getItems() {
        return items;
    }
}
