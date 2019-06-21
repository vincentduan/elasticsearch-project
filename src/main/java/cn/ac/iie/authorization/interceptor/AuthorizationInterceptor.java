package cn.ac.iie.authorization.interceptor;

import cn.ac.iie.authorization.annotation.Authorization;
import cn.ac.iie.authorization.manager.TokenManager;
import cn.ac.iie.authorization.model.TokenModel;
import cn.ac.iie.service.PrivilegesService;
import cn.ac.iie.utils.CommonUtils;
import cn.ac.iie.utils.Constants;
import cn.ac.iie.utils.ResponseVOUtil;
import cn.ac.iie.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private PrivilegesService privilegesService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println(handler);
        //如果不是映射到方法直接通过
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //从header中得到token
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        //验证token
        TokenModel model = tokenManager.getToken(authorization);
        if (tokenManager.checkToken(model) && privilegesService.checkUser(model.getUserName(), requestURI)) {
            //如果token验证成功，将token对应的用户userName存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, model.getUserName());
            return true;
        }
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
            System.out.println("401");
            /*response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/tokens/info");*/
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            Map<String, Object> map = CommonUtils.beanToMap(ResponseVOUtil.UNAUTHORIZED());
            ResponseWrapper.responseToJson(response, map);
            return false;
        }
        return true;
    }

}
