package cn.ac.iie.service.impl;

import cn.ac.iie.service.TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class Md5TokenGenerator implements TokenGenerator {

    @Override
    public String generateWithTimeMills(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }

    @Override
    public String generate(String... strings) {
        String outputStr = "";
        String inputStr = "";
        for (String s : strings) {
            inputStr = inputStr + s;
        }
        try {
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(inputStr.getBytes("utf-8"));
            int j = digest.length;
            char strArr[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = digest[i];
                strArr[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strArr[k++] = hexDigits[byte0 & 0xf];
            }
            for (char c : strArr) {
                outputStr = outputStr + c;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return outputStr;
    }


}
