
package com.paradise.monitor.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密工具
 * @ClassName  AESUtil 
 * @author  llwu
 * @date  2017年12月7日 下午1:56:43
 */
public class AESUtil {
	/** 日志  */
	private static Logger logger = LoggerFactory.getLogger(AESUtil.class);

	/** 默认的加密形式 */
	private static final String KEY_ALGORITHM = "AES";
	
	/** 默认的加密算法 */
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	
	/**
	 * AES加密
	 * @Title  encrypt 
	 * @author  llwu
	 * @param content
	 * @param password
	 * @return  String
	 */
	public static String encrypt(String content, String password) {
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(password)){
			return null;
		}
        try {
        	//创建密码器 
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            //初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            //加密 
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64.encodeBase64String(result);
        } catch (Exception ex) {
        	logger.error("AES加密", ex);
        }
        return null;
    }
	
	/**
	 * AES解密
	 * @Title  decrypt 
	 * @author  llwu
	 * @param content
	 * @param password
	 * @return  String
	 */
	public static String decrypt(String content, String password) {
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(password)){
			return null;
		}
		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			return new String(result, "utf-8");
		} catch (Exception ex) {
			logger.error("AES解密", ex);
		}
		return null;
	}
	
    /**
     *  生成加密秘钥
     * @Title  getSecretKey 
     * @author  llwu
     * @param password
     * @return
     * @return  SecretKeySpec
     */
	private static SecretKeySpec getSecretKey(final String password) {
        try {
        	//返回生成指定算法密钥生成器的 KeyGenerator 对象
        	KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            //转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
        	logger.error("生成加密秘钥", ex);
        }
        return null;
    }
	
}
