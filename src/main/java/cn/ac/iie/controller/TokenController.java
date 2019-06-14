package cn.ac.iie.controller;

import cn.ac.iie.authorization.annotation.Authorization;
import cn.ac.iie.authorization.manager.TokenManager;
import cn.ac.iie.authorization.model.TokenModel;
import cn.ac.iie.bean.User;
import cn.ac.iie.exception.MyException;
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

    @PostMapping
    public ResponseResult login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        try{
            User user = userService.getByUserName(userName);
            if(!user.getPasswordEn().equals(password)){
                return ResponseVOUtil.error("wrong password");
            }
            TokenModel model = tokenManager.createToken(user.getUserName());
            return ResponseVOUtil.success(model);
        } catch (MyException e) {
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
