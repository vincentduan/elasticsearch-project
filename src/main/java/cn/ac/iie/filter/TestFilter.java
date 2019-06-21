package cn.ac.iie.filter;

import cn.ac.iie.authorization.manager.TokenManager;
import cn.ac.iie.authorization.model.TokenModel;
import cn.ac.iie.service.PrivilegesService;
import cn.ac.iie.utils.CommonUtils;
import cn.ac.iie.utils.Constants;
import cn.ac.iie.utils.ResponseVOUtil;
import cn.ac.iie.utils.ResponseWrapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@Component
public class TestFilter extends ZuulFilter {

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private PrivilegesService privilegesService;

    @Override
    public String filterType() {
        System.out.println("pre=====");
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 是否执行该过滤器，此处为true，说明需要过滤
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        String requestURI = request.getRequestURI();
        System.out.println("======start Filter==========");
        System.out.println("myheader: " + request.getHeader(Constants.AUTHORIZATION));
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        //验证token
        TokenModel model = tokenManager.getToken(authorization);
        if(authorization == null) {
            currentContext.setSendZuulResponse(false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            /*try {
                response.sendRedirect("/tokens/info");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Map<String, Object> map = CommonUtils.beanToMap(ResponseVOUtil.UNAUTHORIZED());
            ResponseWrapper.responseToJson(response, map);
            return false;
        } else if (tokenManager.checkToken(model) && privilegesService.checkUser(model.getUserName(), requestURI)) {
            //如果token验证成功，将token对应的用户userName存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, model.getUserName());
            return null;
        }
        // 否则正常执行 调用服务接口...
        return null;
    }
}
