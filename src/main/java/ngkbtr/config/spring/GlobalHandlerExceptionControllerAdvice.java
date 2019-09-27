package ngkbtr.config.spring;

import ngkbtr.model.exception.FailedAuthenticationException;
import ngkbtr.model.exception.ForbiddenResourceException;
import ngkbtr.model.exception.TokenExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalHandlerExceptionControllerAdvice {

    @ExceptionHandler({Exception.class})
    public Object handleError(Exception exception, HttpServletResponse response) throws IOException {
        if (exception instanceof FailedAuthenticationException || exception instanceof TokenExpiredException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (exception instanceof ForbiddenResourceException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        response.getWriter().write(exception.getMessage());
        return null;
    }
}
