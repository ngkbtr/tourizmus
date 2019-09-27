package ngkbtr.common.auth.model;

import java.io.Serializable;

public class Token implements Serializable {
    private String token;
    private String expiration;

    public Token(String token, String expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public String getExpiration() {
        return expiration;
    }
}
