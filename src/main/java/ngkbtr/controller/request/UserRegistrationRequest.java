package ngkbtr.controller.request;

import javax.validation.constraints.NotNull;

public class UserRegistrationRequest extends UserAuthRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
}
