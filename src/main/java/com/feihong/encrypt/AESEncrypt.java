package com.feihong.encrypt;

import org.apache.shiro.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;

public class AESEncrypt {
    public static String encrypt(String key, byte[] payload){
        try{
            byte[] raw = Base64.decode(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameters params = cipher.getParameters();
            byte[] ivs = params.getParameterSpec(IvParameterSpec.class).getIV();
            IvParameterSpec iv = new IvParameterSpec(ivs);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(payload);

            byte[] result = new byte[ivs.length + encrypted.length];
            System.arraycopy(ivs,0, result, 0, ivs.length);
            System.arraycopy(encrypted, 0, result, ivs.length, encrypted.length);

            return Base64.encodeToString(result);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
