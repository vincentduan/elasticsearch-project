package cn.ac.iie.service.impl;

import cn.ac.iie.bean.User;
import cn.ac.iie.exception.MyException;
import cn.ac.iie.service.RsaService;
import cn.ac.iie.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RsaServiceImpl implements RsaService {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private Md5TokenGenerator md5TokenGenerator;

    @Override
    public String getPublicKey() throws NoSuchAlgorithmException {
        //生成公钥和私钥
        Map<String, String> keyMap = genKeyPair();
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        redisTemplate.opsForValue().set(publicKey, privateKey, 10, TimeUnit.SECONDS);
        return publicKey;
    }

    @Override
    public boolean verifyPassword(String userName, String passwordEn, String publicKey) throws Exception {
        String privateKey = redisTemplate.opsForValue().get(publicKey);
        redisTemplate.opsForValue().getOperations().delete(publicKey);
        String passwordDe = decrypt(passwordEn, privateKey);
        passwordDe = md5TokenGenerator.generate(passwordDe);
        String realPassword = userService.getByUserName(userName).getPasswordEn();
        return realPassword.equals(passwordDe);
    }

    @Override
    public DocWriteResponse setPassword(String userName, String passwordEn, String publicKey, boolean reset) throws Exception {
        String privateKey = redisTemplate.opsForValue().get(publicKey);
        redisTemplate.opsForValue().getOperations().delete(publicKey);
        String passwordDe = decrypt(passwordEn, privateKey);
        passwordDe = md5TokenGenerator.generate(passwordDe);
        if (!reset) { // 添加新用户，并设置密码
            User user = userService.getByUserName(userName);
            if (user != null) {
                throw new MyException(" The userName is already exists");
            }
            IndexResponse indexResponse = userService.addUser(new User(userName, passwordDe));
            return indexResponse;
        } else { // 旧用户修改密码
            User user = userService.getByUserName(userName);
            if (user == null) {
                throw new MyException(" The userName don't exists");
            }
            UpdateResponse updateResponse = userService.updateUser(new User(userName, passwordDe));
            return updateResponse;
        }


    }


    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    private static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        Map<String, String> keyMap = new HashMap<>();

        keyMap.put("publicKey", publicKeyString);  //0表示公钥
        keyMap.put("privateKey", privateKeyString);  //1表示私钥

        return keyMap;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    private static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    private static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}