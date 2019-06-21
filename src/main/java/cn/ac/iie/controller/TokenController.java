package cn.ac.iie.controller;

import cn.ac.iie.authorization.annotation.Authorization;
import cn.ac.iie.authorization.manager.TokenManager;
import cn.ac.iie.authorization.model.TokenModel;
import cn.ac.iie.bean.User;
import cn.ac.iie.exception.MyException;
import cn.ac.iie.service.RsaService;
import cn.ac.iie.service.UserService;
import cn.ac.iie.utils.ResponseVOUtil;
import cn.ac.iie.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    UserService userService;
    @Autowired
    TokenManager tokenManager;
    @Autowired
    RsaService rsaService;

    @PostMapping
    public ResponseResult login(@RequestParam("userName") String userName, @RequestParam("passwordEn") String passwordEn, @RequestParam("publicKey") String publicKey) {
        try{
            User user = userService.getByUserName(userName);
            /*if(!user.getPasswordEn().equals(password)){
                return ResponseVOUtil.error("wrong password");
            }*/
            if(rsaService.verifyPassword(userName, passwordEn, publicKey)) {
                TokenModel model = tokenManager.createToken(userName);
                return ResponseVOUtil.success(model);
            } else {
                return ResponseVOUtil.error("wrong username or password");
            }
        } catch (MyException e) {
            return ResponseVOUtil.exception(e.getMessage());
        } catch (Exception e) {
            return ResponseVOUtil.exception(e.getMessage());
        }
    }

    @DeleteMapping
    @Authorization
    public ResponseResult logout(User user) {
        tokenManager.deleteToken(user.getUserName());
        return ResponseVOUtil.success("success");
    }

    @GetMapping("/info")
    public ResponseResult loginInfo() {
        return ResponseVOUtil.failed(HttpStatus.UNAUTHORIZED);
    }

}
