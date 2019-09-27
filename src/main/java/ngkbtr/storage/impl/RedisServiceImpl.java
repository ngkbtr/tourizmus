package ngkbtr.storage.impl;

import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.storage.IStorageService;
import ngkbtr.storage.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisServiceImpl implements IStorageService {

    private static final String USER_TABLE_KEY = "users";
    private static final String SECURITY_TABLE_KEY = "security";
    private static final String TRIP_TABLE_KEY = "trips";
    private static final String CLAIM_TABLE_KEY = "claims";
    private static final long CLAIM_TTL_IM_SECONDS = 60;

    private final RedisDao dao;

    @Autowired
    public RedisServiceImpl(RedisDao dao) {
        this.dao = dao;
    }

    @Override
    public User getUserById(String id) {
        return dao.get(USER_TABLE_KEY, id);
    }

    public void saveUser(User user) {
        if(user == null){
            throw new RuntimeException("User must not be null");
        }
        dao.save(USER_TABLE_KEY, user.getUid(), user);
    }

    public Map<String, User> getAllUsers() {
        return dao.getAllGroup(USER_TABLE_KEY);
    }

    //SECURITY

    @Override
    public SecurityInfo getSecurityInfoById(String uid) {
        return dao.get(SECURITY_TABLE_KEY, uid);
    }

    @Override
    public void saveSecurityInfo(SecurityInfo info) {
        dao.save(SECURITY_TABLE_KEY, info.getUid(), info);
    }
}
