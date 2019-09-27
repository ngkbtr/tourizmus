package ngkbtr.storage;

import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;

import java.util.Map;

public interface IStorageService {

    User getUserById(String uid);

    Map<String, User> getAllUsers();

    void saveUser(User user);

    //SECURITY

    void saveSecurityInfo(SecurityInfo info);

    SecurityInfo getSecurityInfoById(String uid);
}
