package ngkbtr.controller.request;

import ngkbtr.model.location.Location;

import javax.validation.constraints.NotNull;

public class UserAuthRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Location location;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
