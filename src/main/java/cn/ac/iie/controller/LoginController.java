package cn.ac.iie.controller;

import cn.ac.iie.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping
    public ResponseResult login(@RequestParam String username, @RequestParam String password) {
        return null;
    }

}
