package cn.ac.iie.authorization.manager.impl;

import cn.ac.iie.authorization.manager.TokenManager;
import cn.ac.iie.authorization.model.TokenModel;
import cn.ac.iie.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public TokenModel createToken(String userName) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(userName, token);
//        redisTemplate.boundValueOps(userName).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        redisTemplate.boundValueOps(userName).set(token, Constants.TOKEN_EXPIRES_SECOND, TimeUnit.SECONDS);
        return model;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = redisTemplate.opsForValue().get(model.getUserName());
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        // redisTemplate.boundValueOps(model.getUserName()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        redisTemplate.boundValueOps(model.getUserName()).expire(Constants.TOKEN_EXPIRES_SECOND, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        //使用userName和源token简单拼接成的token，可以增加加密措施
        String userName = param[0];
        String token = param[1];
        return new TokenModel(userName, token);
    }

    @Override
    public void deleteToken(String userName) {
        redisTemplate.delete(userName);
    }


}
