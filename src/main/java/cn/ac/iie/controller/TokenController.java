package cn.ac.iie.controller;

import cn.ac.iie.service.UserService;
import cn.ac.iie.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseResult login(@RequestParam String username, @RequestParam String password) {
        userService.getByUserName(username);
        return null;
    }

}
