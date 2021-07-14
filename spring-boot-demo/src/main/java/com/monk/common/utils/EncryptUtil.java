package com.monk.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密常用类
 * 
 * @author huangyulan
 * @version V1.0
 * @date 2018年3月6日 上午10:46:36
 */
@SuppressWarnings("restriction")
public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 密钥是16位长度的byte[]进行Base64转换后得到的字符串
     */
    public static String key = "LmMGStGtOpF4xNyvYt54EQ==";
    
    private static final String DES_PREFIX = "{DES}";

    /**
     * <li>
     * 方法名称:encrypt</li> <li>
     * 加密方法
     * 
     * @param xmlStr
     *            需要加密的消息字符串
     * @return 加密后的字符串
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static String encrypt(String xmlStr) {
        byte[] encrypt = null;

        try {
            // 取需要加密内容的utf-8编码。
            encrypt = xmlStr.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        // 取MD5Hash码，并组合加密数组
        byte[] md5Hasn = null;
        try {
            md5Hasn = EncryptUtil.MD5Hash(encrypt, 0, encrypt.length);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        // 组合消息体
        byte[] totalByte = EncryptUtil.addMD5(md5Hasn, encrypt);

        // 取密钥和偏转向量
        byte[] key = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(EncryptUtil.key, key, iv);
        SecretKeySpec deskey = new SecretKeySpec(key, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法使用加密消息体
        byte[] temp = null;
        try {
            temp = EncryptUtil.DES_CBC_Encrypt(totalByte, deskey, ivParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 使用Base64加密后返回
        return new BASE64Encoder().encode(temp);
    }

    /**
     * <li>
     * 方法名称:encrypt</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 解密方法
     * </pre>
     * 
     * </li>
     * 
     * @param xmlStr
     *            需要解密的消息字符串
     * @return 解密后的字符串
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static String decrypt(String xmlStr) throws Exception {
        // base64解码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] encBuf = null;
        try {
            encBuf = decoder.decodeBuffer(xmlStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // 取密钥和偏转向量
        byte[] key = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(EncryptUtil.key, key, iv);

        SecretKeySpec deskey = new SecretKeySpec(key, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法解密
        byte[] temp = null;
        try {
            temp = EncryptUtil.DES_CBC_Decrypt(encBuf, deskey, ivParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 进行解密后的md5Hash校验
        byte[] md5Hash = null;
        try {
            md5Hash = EncryptUtil.MD5Hash(temp, 16, temp.length - 16);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 进行解密校检
        for (int i = 0; i < md5Hash.length; i++) {
            if (md5Hash[i] != temp[i]) {
                throw new Exception("MD5校验错误。");
            }
        }

        // 返回解密后的数组，其中前16位MD5Hash码要除去。
        return new String(temp, 16, temp.length - 16, "utf-8");
    }

    /**
     * <li>
     * 方法名称:TripleDES_CBC_Encrypt</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 经过封装的三重DES/CBC加密算法，如果包含中文，请注意编码。
     * </pre>
     * 
     * </li>
     * 
     * @param sourceBuf
     *            需要加密内容的字节数组。
     * @param deskey
     *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
     * @return 加密后的字节数组
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] TripleDES_CBC_Encrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {
        byte[] cipherByte;
        // 使用DES对称加密算法的CBC模式加密
        Cipher encrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");

        encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

        cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回加密后的字节数组
        return cipherByte;
    }

    /**
     * <li>
     * 方法名称:TripleDES_CBC_Decrypt</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 经过封装的三重DES / CBC解密算法
     * </pre>
     * 
     * </li>
     * 
     * @param sourceBuf
     *            需要解密内容的字节数组
     * @param deskey
     *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
     * @return 解密后的字节数组
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] TripleDES_CBC_Decrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {

        byte[] cipherByte;
        // 获得Cipher实例，使用CBC模式。
        Cipher decrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        // 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
        decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

        cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回解密后的字节数组
        return cipherByte;
    }

    /**
     * <li>
     * 方法名称:DES_CBC_Encrypt</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 经过封装的DES/CBC加密算法，如果包含中文，请注意编码。
     * </pre>
     * 
     * </li>
     * 
     * @param sourceBuf
     *            需要加密内容的字节数组。
     * @param deskey
     *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
     * @return 加密后的字节数组
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] DES_CBC_Encrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {
        byte[] cipherByte;
        // 使用DES对称加密算法的CBC模式加密
        Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");

        encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

        cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回加密后的字节数组
        return cipherByte;
    }

    /**
     * <li>
     * 方法名称:DES_CBC_Decrypt</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 经过封装的DES/CBC解密算法。
     * </pre>
     * 
     * </li>
     * 
     * @param sourceBuf
     *            需要解密内容的字节数组
     * @param deskey
     *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
     * @return 解密后的字节数组
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] DES_CBC_Decrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {

        byte[] cipherByte;
        // 获得Cipher实例，使用CBC模式。
        Cipher decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
        decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

        cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回解密后的字节数组
        return cipherByte;
    }

    /**
     * <li>
     * 方法名称:MD5Hash</li> <li>
     * 功能描述:
     * 
     * <pre>
     * MD5，进行了简单的封装，以适用于加，解密字符串的校验。
     * </pre>
     * 
     * </li>
     * 
     * @param buf
     *            需要MD5加密字节数组。
     * @param offset
     *            加密数据起始位置。
     * @param length
     *            需要加密的数组长度。
     * @return 加密后的字节数组
     * @throws Exception
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] MD5Hash(byte[] buf, int offset, int length) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(buf, offset, length);
        return md.digest();
    }

    /**
     * <li>
     * 方法名称:byte2hex</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 字节数组转换为二行制表示
     * </pre>
     * 
     * </li>
     * 
     * @param inStr
     *            需要转换字节数组。
     * @return 字节数组的二进制表示。
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static String byte2hex(byte[] inStr) {
        String stmp;
        StringBuffer out = new StringBuffer(inStr.length * 2);

        for (int n = 0; n < inStr.length; n++) {
            // 字节做"与"运算，去除高位置字节 11111111
            stmp = Integer.toHexString(inStr[n] & 0xFF);
            if (stmp.length() == 1) {
                // 如果是0至F的单位字符串，则添加0
                out.append("0" + stmp);
            } else {
                out.append(stmp);
            }
        }
        return out.toString();
    }

    /**
     * <li>
     * 方法名称:addMD5</li> <li>
     * 功能描述:
     * 
     * <pre>
     * MD校验码 组合方法，前16位放MD5Hash码。 把MD5验证码byte[]，加密内容byte[]组合的方法。
     * </pre>
     * 
     * </li>
     * 
     * @param md5Byte
     *            加密内容的MD5Hash字节数组。
     * @param bodyByte
     *            加密内容字节数组
     * @return 组合后的字节数组，比加密内容长16个字节。
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static byte[] addMD5(byte[] md5Byte, byte[] bodyByte) {
        int length = bodyByte.length + md5Byte.length;
        byte[] resutlByte = new byte[length];

        // 前16位放MD5Hash码
        for (int i = 0; i < length; i++) {
            if (i < md5Byte.length) {
                resutlByte[i] = md5Byte[i];
            } else {
                resutlByte[i] = bodyByte[i - md5Byte.length];
            }
        }

        return resutlByte;
    }

    /**
     * <li>
     * 方法名称:getKeyIV</li> <li>
     * 功能描述:
     * 
     * <pre>
     * 
     * </pre>
     * </li>
     * 
     * @param encryptKey
     *            加密码字符 串
     * @param key
     *            键字节
     * @param iv
     *            向量字节
     * @author huangyulan
     * @date 2018年3月6日 上午10:46:36
     */
    public static void getKeyIV(String encryptKey, byte[] key, byte[] iv) {
        // 密钥Base64解密
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = null;
        try {
            buf = decoder.decodeBuffer(encryptKey);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        // 前8位为key
        int i;
        for (i = 0; i < key.length; i++) {
            key[i] = buf[i];
        }
        // 后8位为iv向量
        for (i = 0; i < iv.length; i++) {
            iv[i] = buf[i + 8];
        }
    }
    
    /**
     * 替换{DES}前缀并解密密码
     * 
     * @param password
     *            加密的密码
     * @return 解密后的密码
     * @author Monk
     * @date 2021年6月8日 下午3:51:59
     */
    public static String replacePrefixAndDecryptPwd(String password) {
        String decryptPwd = password;
        try {
            if (StringUtils.isNotEmpty(password) && password.startsWith(DES_PREFIX)) {
                password = password.replace(DES_PREFIX, "");
                decryptPwd = decrypt(password);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            decryptPwd = password;
        }
        return decryptPwd;
    }
    
    /**
     * 使用指定的秘钥加密字符串
     * 
     * @param xmlStr
     *            待加密字符串
     * @param encryptKey
     *            秘钥
     * @return 加密后的字符串
     * @author Monk
     * @date 2021年6月17日 下午7:47:26
     */
    public static String encrypt(String xmlStr, String encryptKey) {
        byte[] encrypt = null;

        try {
            // 取需要加密内容的utf-8编码。
            encrypt = xmlStr.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        // 取MD5Hash码，并组合加密数组
        byte[] md5Hasn = null;
        try {
            md5Hasn = EncryptUtil.MD5Hash(encrypt, 0, encrypt.length);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        // 组合消息体
        byte[] totalByte = EncryptUtil.addMD5(md5Hasn, encrypt);

        // 取密钥和偏转向量
        byte[] key = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(encryptKey, key, iv);
        SecretKeySpec deskey = new SecretKeySpec(key, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法使用加密消息体
        byte[] temp = null;
        try {
            temp = EncryptUtil.DES_CBC_Encrypt(totalByte, deskey, ivParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 使用Base64加密后返回
        return new BASE64Encoder().encode(temp);
    }
    
    /**
     * 使用指定的秘钥解密字符串
     * 
     * @param xmlStr
     *            加密的字符串
     * @param encryptKey
     *            秘钥
     * @return 解密后的字符串
     * @throws Exception
     * @author Monk
     * @date 2021年6月17日 下午7:48:12
     */
    public static String decrypt(String xmlStr, String encryptKey) throws Exception {
        // base64解码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] encBuf = null;
        try {
            encBuf = decoder.decodeBuffer(xmlStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // 取密钥和偏转向量
        byte[] key = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(encryptKey, key, iv);

        SecretKeySpec deskey = new SecretKeySpec(key, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法解密
        byte[] temp = null;
        try {
            temp = EncryptUtil.DES_CBC_Decrypt(encBuf, deskey, ivParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 进行解密后的md5Hash校验
        byte[] md5Hash = null;
        try {
            md5Hash = EncryptUtil.MD5Hash(temp, 16, temp.length - 16);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 进行解密校检
        for (int i = 0; i < md5Hash.length; i++) {
            if (md5Hash[i] != temp[i]) {
                throw new Exception("MD5校验错误。");
            }
        }

        // 返回解密后的数组，其中前16位MD5Hash码要除去。
        return new String(temp, 16, temp.length - 16, "utf-8");
    }

}
