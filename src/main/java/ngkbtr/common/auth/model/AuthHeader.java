package ngkbtr.common.auth.model;

public class AuthHeader {
    private String alg;
    private String typ;

    public AuthHeader(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }
}
