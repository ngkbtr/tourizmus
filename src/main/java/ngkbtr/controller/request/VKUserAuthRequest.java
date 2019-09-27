package ngkbtr.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VKUserAuthRequest{
    private VKAuthSession session;
    private String status;

    public VKAuthSession getSession() {
        return session;
    }

    public String getStatus() {
        return status;
    }

    static public class VKAuthSession {
        private Long mid;
        private Long expire;
        private String secret;
        private String sig;
        private String sid;
        private VKUser user;

        public Long getMid() {
            return mid;
        }

        public Long getExpire() {
            return expire;
        }

        public String getSecret() {
            return secret;
        }

        public String getSig() {
            return sig;
        }

        public String getSid() {
            return sid;
        }

        public VKUser getUser() {
            return user;
        }

        public void setMid(Long mid) {
            this.mid = mid;
        }

        public void setExpire(Long expire) {
            this.expire = expire;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public void setSig(String sig) {
            this.sig = sig;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setUser(VKUser user) {
            this.user = user;
        }

        static public class VKUser {
            private String domain;
            @JsonProperty("first_name")
            private String firstName;
            private String href;
            private String id;
            @JsonProperty("last_name")
            private String lastName;
            private String nickname;

            public String getDomain() {
                return domain;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getHref() {
                return href;
            }

            public String getId() {
                return id;
            }

            public String getLastName() {
                return lastName;
            }

            public String getNickname() {
                return nickname;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
