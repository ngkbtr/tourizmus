package ngkbtr.model.auth;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.Token;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class SecurityInfo implements Serializable {
    private String uid;
    private String hashedPassword;
    private Token refreshToken;

    public SecurityInfo(String uid, String password) {
        this.uid = uid;
        this.refreshToken = AuthHelper.createRefreshToken(uid);
        if(StringUtils.hasText(password)) {
            this.hashedPassword = AuthHelper.hashPassword(password);
        }
    }

    public String getUid() {
        return uid;
    }

    public String getRefreshToken() {
        return refreshToken.getToken();
    }

    public boolean isRefreshTokenExpired(){
        return new Date(Long.valueOf(refreshToken.getExpiration())).before(new Date());
    }

    public Token updateRefreshToken(String uid) {
        this.refreshToken = AuthHelper.createRefreshToken(uid);
        return this.refreshToken;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
