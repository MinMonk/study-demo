/**
 * 文件名： GZIPUtil.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： SOA管控系统
 */

package com.monk.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gzip字符串压缩接压缩工具类
 * 
 * @author houyongchuan
 * @date 2018年12月4日 上午10:08:04
 */
public class GZIPUtil {
    private static final Logger logger = LoggerFactory.getLogger(GZIPUtil.class);

    public static final String GZIP_PERFIX = "{GZIP}";
    
    
    public static void main(String[] args) {
        String str = "{GZIP}H4sIAAAAAAAAALWWzW7TQBDH70i8Q+QjKNl14n5ZjpHjbBrTOHa9ayC9rCxnCZEaO4rdNtxREQJRDhVIcIITQqLiCFx4mZbQt2ATO40bUhBSauVgz/znl9nx7HiVO8Pebm6fDaJuGJQFsQCFHAv8sN0NOmXBJbX8unBHvXlDiUKvz4J9GQX7bDfssxyPCyI5NZeFR3HclwGI/Ees50UF7h27CuGgA8Y3gKVxQFAvWHXmtdkAJAa5ErYfZ6j/hbyR45cSREXZCPp7se0NvB6L+apSIPfMeKFX8Hu+X/DDHrBwhVZsii2N1rep0euHg7jS7sQO22VexJxwL2Z4sC8knMUMM+okK0nzSDMx8WYdaVXkTK3cji3X0RFuYYJMo6pWTE0Bc7bF4qZmIvX83eHp189nL16ff3gyOnkzOnk/enV49v14dPzxMmainoGItYWa6q3JpYDkaeZ1MXKoUc0No64cdHfLQjzYY+mCZW68WPTBwUHhoDSpfxFCETwwG3jybvLdIIq9wGcCmOeOM7kesq1hfH/5WWO3YhqEVjWC1CIsinlYysMNAtdlcU0uwcJ6ae02f4CQVzwjnQFsbRNRbOwsf9m66zioSej4H5YOJxbRGtRBuuUsv6i2Y90zmjqiulVFan1bAZctM6VjuSQ1anUFZB4zqToaD7yGjnUQb6x7qErF60MXl4tWwMJRk4zCpu0S3Wo0kE4MK7vlF3ipwefGTJKKbMegW6ilbrQlT1z1HkorUJRWxZLnez5kRcjWVkUmFdeTNKbqeUrV0l1z3LmkZU97gNBaq7Z1NwlcILiS0XRNFcKSNN6cUBJXIFydg4wVV4ZP4Lqp1fOVnfzVjL/nUEVYdwx7UtVfTz+dnbw9/fbs59HRrx9fhsPhHCorvjotB/ExQistdfTy+fm3owUZXSjmIXw/NHGNj0RimNOhJeXFFSJCmf8kWIAQTofWnwHzOKxb/CXUGtqmKiX6jOUP8eSrQ5PPDq8rTiMumeeDJq1H0QOy7G2WLYiWtHXDaCKc3YngX82/UJGeMMCCI8bYpYCLI4ya3GfPSupvXY/fz2YJAAA=";
        String unStr = unzipString(str);
        System.out.println(unStr);
    }
    

    /**
     * 对输入的字符串报文进行gzip压缩，返回GZIP_PERFIX+base46编码字符串 如压缩失败返回源字符串
     * 
     * @param xml
     *            字符串报文
     * @return gzip压缩后base64编码字符串
     * @date 2018年12月4日 上午10:11:00
     */
    public static String zipString(String xml) {
        if (xml == null || xml.length() == 0) {
            return xml;
        }
        GZIPOutputStream gout = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            gout = new GZIPOutputStream(buffer);
            gout.write(xml.getBytes());
            gout.flush();
            gout.close();
            gout = null;
            byte[] result = buffer.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(result);
            return GZIP_PERFIX + base64;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (gout != null) {
                    gout.close();
                }
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return xml;
    }

    /**
     * 对输入的字节码数组进行gzip压缩，返回GZIP_PERFIX+base46编码字符串 如压缩失败返回源字符串
     * 
     * @param xmlBytes
     *            字节码报文
     * @param chartSet
     *            字符编码
     * @return gzip压缩后base64编码字符串
     * @date 2018年12月4日 上午10:13:45
     */
    public static String zipBytes(byte[] xmlBytes, String chartSet) {
        if (xmlBytes == null || xmlBytes.length == 0) {
            return "";
        }
        GZIPOutputStream gout = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            gout = new GZIPOutputStream(buffer);
            gout.write(xmlBytes);
            gout.flush();
            gout.close();
            gout = null;
            byte[] result = buffer.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(result);
            return GZIP_PERFIX + base64;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (gout != null) {
                    gout.close();
                }
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return new String(xmlBytes);
    }

    /**
     * 对zipString(String xml)方式压缩的报文进行解压 如压缩失败返回源字符串
     * 
     * @param zipString
     *            压缩报文字符串
     * @return 解压报文字符串
     * @date 2018年12月4日 上午10:17:11
     */
    public static String unzipString(String zipString) {
        if (zipString == null || zipString.length() == 0) {
            return "";
        }

        if (!zipString.startsWith(GZIP_PERFIX)) {
            return zipString;
        }
        zipString = zipString.substring(GZIP_PERFIX.length());
        GZIPInputStream gin = null;
        ByteArrayOutputStream out = null;
        try {
            byte[] xmlByte = Base64.getDecoder().decode(zipString);
            ByteArrayInputStream xmlBytein = new ByteArrayInputStream(xmlByte);
            gin = new GZIPInputStream(xmlBytein);
            out = new ByteArrayOutputStream();
            byte[] bufferout = new byte[1024];
            int offset = -1;
            while ((offset = gin.read(bufferout)) != -1) {
                out.write(bufferout, 0, offset);
            }
            return out.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (gin != null) {
                    gin.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }

            gin = null;
            out = null;
        }
        return zipString;
    }
}
