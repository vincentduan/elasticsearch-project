package cn.ac.iie.controller;

import cn.ac.iie.authorization.annotation.Authorization;
import cn.ac.iie.service.RsaService;
import cn.ac.iie.utils.ResponseVOUtil;
import cn.ac.iie.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.NoSuchAlgorithmException;

@RestController
public class RsaController {

    private Logger logger = LoggerFactory.getLogger(RsaController.class);

    @Autowired
    RsaService rsaService;

    @GetMapping(value = "/rsa/getPublicKey")
    public Object getPublicKey () {
        try {
            return ResponseVOUtil.success(rsaService.getPublicKey());
        } catch (NoSuchAlgorithmException e) {
            logger.info(e.getMessage());
            return ResponseVOUtil.failed( e.getMessage());
        }
    }

    @PostMapping(value = "/userManagement/rsa/verifyPassword")
    @Authorization
    public Object verifyPassword(@RequestParam("userName") String userName, @RequestParam("passwordEn") String passwordEn, @RequestParam("publicKey") String publicKey) throws Exception {
        boolean verifyPassword = rsaService.verifyPassword(userName, passwordEn, publicKey);
        return ResponseVOUtil.success(verifyPassword);
    }

    @PutMapping(value = "/userManagement/rsa/setPassword")
    @Authorization
    public Object setPassword (@RequestParam("userName") String userName, @RequestParam("passwordEn") String passwordEn, @RequestParam("publicKey") String publicKey, @RequestParam("reset") boolean reset) {
        String result = null;
        try {
            result = rsaService.setPassword(userName, passwordEn, publicKey, reset).getResult().toString();
            return ResponseVOUtil.success(result);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseVOUtil.success(e.getMessage());
        }

    }

}
