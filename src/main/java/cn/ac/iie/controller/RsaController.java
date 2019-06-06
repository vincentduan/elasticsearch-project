package cn.ac.iie.controller;

import cn.ac.iie.service.RsaService;
import cn.ac.iie.web.ResponseResult;
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
            return new ResponseResult("200","success", rsaService.getPublicKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new ResponseResult("500","failed", e.getMessage());
        }
    }

    @PostMapping(value = "/rsa/verifyPassword")
    public Object verifyPassword(@RequestParam("userName") String userName, @RequestParam("passwordEn") String passwordEn, @RequestParam("publicKey") String publicKey) throws Exception {
        boolean verifyPassword = rsaService.verifyPassword(userName, passwordEn, publicKey);
        return new ResponseResult("200", "success", verifyPassword);
    }

    @PutMapping(value = "/rsa/setPassword")
    public Object setPassword (@RequestParam("userName") String userName, @RequestParam("passwordEn") String passwordEn, @RequestParam("publicKey") String publicKey, @RequestParam("reset") boolean reset) {
        String result = null;
        try {
            result = rsaService.setPassword(userName, passwordEn, publicKey, reset).getResult().toString();
            return new ResponseResult("200", "success", result);
        } catch (Exception e) {
            return new ResponseResult("500", "failed", e.getMessage());
        }

    }

}
