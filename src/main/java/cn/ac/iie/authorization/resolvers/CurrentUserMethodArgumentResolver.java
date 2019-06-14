package cn.ac.iie.authorization.resolvers;

import cn.ac.iie.authorization.annotation.CurrentUser;
import cn.ac.iie.bean.User;
import cn.ac.iie.service.UserService;
import cn.ac.iie.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Component
public class CurrentUserMethodArgumentResolver  implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果参数类型是User并且有CurrentUser注解则支持
        if (parameter.getParameterType().isAssignableFrom(User.class) &&
                parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //取出鉴权时存入的登录用户Id
        String currentUserName = webRequest.getAttribute(Constants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST).toString();
        if (currentUserName != null) {
            //从数据库中查询并返回
            return userService.getByUserName(currentUserName);
        }
        throw new MissingServletRequestPartException(Constants.CURRENT_USER_ID);
    }
}
