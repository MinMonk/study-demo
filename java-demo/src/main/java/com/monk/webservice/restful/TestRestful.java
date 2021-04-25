/**
 * 
 * 文件名：TestRestful.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.webservice.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.common.utils.StringUtils;

import net.sf.json.JSONObject;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年9月10日 下午4:25:32
 */
public class TestRestful {
    private static final Logger logger = LoggerFactory.getLogger(TestRestful.class);

    public static void main(String[] args) {
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "admin");
        paramMap.put("password", "123456");
        paramMap.put("type", "6");
        paramMap.put("sign", "897b6add0b079f16070c1c1fb86ad782");
        paramMap.put("destURL", "index.do");
        
//        logger.info(invokeService("http://1zm8875695.iok.la:36271/iotview/IOT_CDP_C01_AuthLogin", paramMap).toString());
        logger.info(login("http://1zm8875695.iok.la:36271/iotview/IOT_CDP_C01_AuthLogin").toString());
//        logger.info(login("http://10.204.103.243:8088/authLogin").toString());
    }

    public static Map<String, Object> login(String httpAddress) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream inStream = null;
        try {
            /*MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("userName", "admin");
            builder.addTextBody("password", "123456");
            builder.addTextBody("type", "6");
            builder.addTextBody("sign", "897b6add0b079f16070c1c1fb86ad782");
            builder.addTextBody("destURL", "index.do");
            HttpEntity entity = builder.build();*/
            if (!StringUtils.isNotEmpty(httpAddress)) {
                result.put("code", -1);
                result.put("message", "请求地址为空");
            }
            if (!httpAddress.endsWith("/")) {
                httpAddress += "/";
            }
            HttpPost request = new HttpPost(httpAddress);
            
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
            nvps.add(new BasicNameValuePair("userName", "admin"));  
            nvps.add(new BasicNameValuePair("password", "123456"));
            nvps.add(new BasicNameValuePair("type", "6"));
            nvps.add(new BasicNameValuePair("sign", "897b6add0b079f16070c1c1fb86ad782"));
            nvps.add(new BasicNameValuePair("destURL", "index.do"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
            entity.setContentType("application/json");
            request.setEntity(entity);
            HttpClient client = HttpClients.createDefault();

            HttpResponse response = client.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                inStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder responseContext = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseContext.append(line);
                }
                inStream.close();
                JSONObject jsonObj = JSONObject.fromObject(responseContext.toString());
                Iterator it = jsonObj.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    result.put(key, jsonObj.get(key));
                }
            }else {
                result.put("code", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", -2);
            result.put("message", e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) {
                    logger.error("close input stream failed.", e);
                }
            }
        }
        return result;
    }
    
    
    public static Map<String, Object> invokeService(String httpAddress, Map<String, String> paramMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream inStream = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for(String str : paramMap.keySet()) {
                builder.addTextBody(str, paramMap.get(str));
            }
            if (!StringUtils.isNotEmpty(httpAddress)) {
                result.put("code", -1);
                result.put("message", "请求地址为空");
            }
            if (!httpAddress.endsWith("/")) {
                httpAddress += "/";
            }
            HttpEntity entity = builder.build();
            HttpPost request = new HttpPost(httpAddress);
            request.setEntity(entity);
            HttpClient client = HttpClients.createDefault();

            HttpResponse response = client.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                inStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "GBK"));
                StringBuilder responseContext = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseContext.append(line);
                }
                inStream.close();
                JSONObject jsonObj = JSONObject.fromObject(responseContext.toString());
                Iterator it = jsonObj.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    result.put(key, jsonObj.get(key));
                }
            }else {
                result.put("code", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", -2);
            result.put("message", e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) {
                    logger.error("close input stream failed.", e);
                }
            }
        }
        return result;
    }
}
