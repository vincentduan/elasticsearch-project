package cn.ac.iie.service;

import org.elasticsearch.action.DocWriteResponse;

import java.security.NoSuchAlgorithmException;

public interface RsaService {
    String getPublicKey() throws NoSuchAlgorithmException;

    boolean verifyPassword(String userName, String passwordEn, String publicKey) throws Exception;

    DocWriteResponse setPassword(String userName, String passwordEn, String publicKey, boolean reset) throws Exception;
}

