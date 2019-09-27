package ngkbtr.config.spring;

import ngkbtr.common.auth.AuthHelper;
import ngkbtr.common.auth.model.AuthPayload;
import ngkbtr.model.User;
import ngkbtr.model.auth.AuthUser;
import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.UserNotFoundException;
import ngkbtr.storage.IStorageService;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static ngkbtr.common.auth.AuthHelper.parseTokenPayload;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final IStorageService storageService;

    public AuthUserHandlerMethodArgumentResolver(IStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(AuthUser.class) != null && methodParameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (this.supportsParameter(methodParameter)) {
            String accessToken = nativeWebRequest.getHeader("Bearer");

            if (StringUtils.hasText(accessToken)) {
                AuthHelper.validateAccessToken(accessToken);
                AuthPayload payload = parseTokenPayload(accessToken);
                User user = storageService.getUserById(payload.getUid());
                if (user != null) {
                    return user;
                } else {
                    throw new UserNotFoundException();
                }
            } else {
                throw new FailedAuthenticationException();
            }
        }
        return WebArgumentResolver.UNRESOLVED;
    }
}
