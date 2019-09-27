package ngkbtr.services;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.controller.request.VKUserAuthRequest;
import ngkbtr.model.User;
import ngkbtr.model.auth.SecurityInfo;
import ngkbtr.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class ValidationService {

    private static final String VK_APP_SECRET_KEY = "HXb9yTCHaK2JanZozIJj";

    private final IStorageService storageService;

    @Autowired
    public ValidationService(IStorageService storageService) {
        this.storageService = storageService;
    }

    public boolean checkUserAlredyExists(String login){
        String uid = AuthHelper.createUserId(login);
        User user = storageService.getUserById(uid);
        return user != null;
    }

    public boolean checkUserPassword(SecurityInfo securityInfo, String password){
        String actualPasswordHash = AuthHelper.hashPassword(password);
        return securityInfo.getHashedPassword().equals(actualPasswordHash);
    }

    public boolean validateVkAuthUser(VKUserAuthRequest request){
        if("connected".equals(request.getStatus())) {
            StringBuilder sb = new StringBuilder();
            sb.append("expire=").append(request.getSession().getExpire())
                    .append("mid=").append(request.getSession().getMid())
                    .append("secret=").append(request.getSession().getSecret())
                    .append("sid=").append(request.getSession().getSid())
                    .append(VK_APP_SECRET_KEY);
            String expectedSignature = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
            return expectedSignature.equals(request.getSession().getSig());
        }
        return false;
    }
}
