package com.feihong.encrypt;

import org.apache.shiro.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class GCMEncrypt {
    public static String encrypt(String key, byte[] payload){
        try{
            byte[] raw = Base64.decode(key);
            byte[] iv = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec params = new GCMParameterSpec(128, iv);

            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, params);
            byte[] encrypted = cipher.doFinal(payload);

            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv,0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return Base64.encodeToString(result);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
