package ngkbtr.common.auth.model;

public class AuthPayload {
    private String uid;
    private String expiration;

    public AuthPayload(String uid, String timestamp) {
        this.uid = uid;
        this.expiration = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getExpiration() {
        return expiration;
    }
}
