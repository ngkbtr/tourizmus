package ngkbtr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ngkbtr.model.location.LocationInfo;
import ngkbtr.model.user.IHasUserInfo;
import ngkbtr.model.user.UserInfo;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements IHasUserInfo, Serializable {

    private String uid;

    private LocationInfo locationInfo;
    private UserInfo userInfo;

    private User(){}

    public User(String uid) {
        this.uid = uid;
    }

    @Override
    public final String getUid() {
        return uid;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }
}
