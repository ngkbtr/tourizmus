package ngkbtr.common.auth;

import com.google.gson.Gson;
import ngkbtr.common.auth.model.AuthHeader;
import ngkbtr.common.auth.model.AuthPayload;
import ngkbtr.common.auth.model.Token;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.TokenExpiredException;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthHelper {

    private static final String AUTH_TOKEN_SECRET_KEY = "auth_sec_key";
    private static final String REFRESH_TOKEN_SECRET_KEY = "ref_sec_key";

    private static final Gson GSON = new Gson();

    public static String createUserId(String source) {
        if (StringUtils.isEmpty(source)) {
            throw new ValidationException("Login must not be empty");
        }
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }

    public static Token createAccessToken(String uid) {
        return createAccessToken(uid, createAccessTokenExpiration());
    }

    private static Token createAccessToken(String uid, String timestamp) {
        try {
            String header = Base64.getUrlEncoder().encodeToString(GSON.toJson(new AuthHeader("HS256", "JWT")).getBytes("UTF-8"));
            String payload = Base64.getUrlEncoder().encodeToString(GSON.toJson(new AuthPayload(uid, timestamp)).getBytes("UTF-8"));
            String unsignedToken = String.format("%s.%s", header, payload);
            Mac Sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(AUTH_TOKEN_SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            Sha256Hmac.init(secretKey);
            String signature = Base64.getUrlEncoder().encodeToString(Sha256Hmac.doFinal(unsignedToken.getBytes("UTF-8")));
            return new Token(String.format("%s.%s.%s", header, payload, signature), createAccessTokenExpiration());
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Token createRefreshToken(String uid) {
        return createRefreshToken(uid, createRefreshTokenExpiration());
    }

    private static Token createRefreshToken(String uid, String timestamp) {
        try {
            String header = Base64.getUrlEncoder().encodeToString(GSON.toJson(new AuthHeader("HS256", "JWT")).getBytes("UTF-8"));
            String payload = Base64.getUrlEncoder().encodeToString(GSON.toJson(new AuthPayload(uid, timestamp)).getBytes("UTF-8"));
            String unsignedToken = String.format("%s.%s", header, payload);
            Mac Sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(REFRESH_TOKEN_SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            Sha256Hmac.init(secretKey);
            String signature = Base64.getUrlEncoder().encodeToString(Sha256Hmac.doFinal(unsignedToken.getBytes("UTF-8")));
            return new Token(String.format("%s.%s.%s", header, payload, signature), createRefreshTokenExpiration());
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static AuthPayload parseTokenPayload(String token) {
        try {
            String[] mass = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(mass[1].getBytes("UTF-8")));
            return GSON.fromJson(payloadJson, AuthPayload.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void validateAccessToken(String accessToken) throws FailedAuthenticationException, TokenExpiredException {
        try {
            String[] mass = accessToken.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(mass[1].getBytes("UTF-8")));
            AuthPayload payload = GSON.fromJson(payloadJson, AuthPayload.class);
            String expectedAuthToken = createAccessToken(payload.getUid(), payload.getExpiration()).getToken();
            if(!expectedAuthToken.equals(accessToken)){
                throw new FailedAuthenticationException();
            }
            if(isTokenExpired(payload.getExpiration())){
                throw new TokenExpiredException();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void validateRefreshToken(String refreshToken) throws FailedAuthenticationException, TokenExpiredException {
        try {
            String[] mass = refreshToken.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(mass[1].getBytes("UTF-8")));
            AuthPayload payload = GSON.fromJson(payloadJson, AuthPayload.class);
            String expectedRefreshToken = createRefreshToken(payload.getUid(), payload.getExpiration()).getToken();
            if(!expectedRefreshToken.equals(refreshToken)){
                throw new FailedAuthenticationException();
            }
            if(isTokenExpired(payload.getExpiration())){
                throw new TokenExpiredException();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String createAccessTokenExpiration() {
        return String.valueOf(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));
    }

    public static String createRefreshTokenExpiration() {
        return String.valueOf(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(60));
    }

    public static String hashPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new ValidationException("Password must not be empty");
        }
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    private static boolean isTokenExpired(String expiration) {
        return new Date(Long.valueOf(expiration)).before(new Date());

    }
}
