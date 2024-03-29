package cn.ac.iie.authorization.manager;

import cn.ac.iie.authorization.model.TokenModel;

public interface TokenManager {
    /**
     * 创建一个token关联上指定用户
     * @param userName 指定用户的id
     * @return 生成的token
     */
     TokenModel createToken(String userName);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
     boolean checkToken(TokenModel model);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
     TokenModel getToken(String authentication);

    /**
     * 清除token
     * @param userName 登录用户的userName
     */
     void deleteToken(String userName);
}
