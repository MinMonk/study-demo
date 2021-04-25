/**
 * 
 * 文件名：Test.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 用httppost的方式上传文件
 * @author Monk
 * @version V1.0
 * @date 2021年3月8日 上午10:33:32
 */
public class UploadFileByHttpPostDemo {

    private static String fileUploadUrl = "http://localhost:8180/file";

    public static void main(String[] args) throws Exception {
        String path = "D:\\apps\\lightcsb\\resource\\userAuditLog\\SOA_WHOLE_ACCOUNT_AUDIT_01_01_20210305190433559.zip";
        uploadAuditLog(path);
    }

    private static void uploadAuditLog(String path) throws Exception {

        Map<String, String> headerParams = new HashMap<String, String>();
        // 必传
        headerParams.put("method", "M_ABILITY_UNIDOC_105000001");
        // 必传，固定值
        headerParams.put("format", "json");
        // 必传-同token地址中的appid
        headerParams.put("appId", "appId");
        // 必传，固定值
        headerParams.put("version", "V1.0");
        // 必传
        headerParams.put("accessToken", "accessToken");

        Map<String, String> busiParams = new HashMap<String, String>();
        busiParams.put("securityLevel", "C");
        // 必传固定值N
        busiParams.put("watermark", "N");
        // 非必传（watermark为Y时，必传）等级固定值1
        busiParams.put("waterMarkId", "1");
        // 必传 固定值-1
        busiParams.put("menuId", "-1");
        // 必传 固定值N
        busiParams.put("fileLock", "N");
        // 非必传（fileLock为Y时，必传）
        busiParams.put("lockPassWord", "1");
        // 必传归档时间传当前时间即可
        busiParams.put("archiveDate", "2021-03-15 18:46:35");
        // 必传固定值N
        busiParams.put("createIndex", "N");

        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        String fileName = file.getName();

        HttpPost httpPost = new HttpPost(fileUploadUrl);

        if (null != headerParams && headerParams.size() > 0) {
            for (Entry<String, String> entry : headerParams.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        Set<String> busiParamsKeySet = busiParams.keySet();
        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        Iterator busIterator = busiParamsKeySet.iterator();
        while (busIterator.hasNext()) {
            String busiParamKey = (String) busIterator.next();
            multipartEntity.addPart(busiParamKey, new StringBody((String) busiParams.get(busiParamKey),
                    ContentType.create("text/plain", Consts.UTF_8)));
        }
        InputStreamBody inputStreamBody = new InputStreamBody(inputStream, fileName);
        multipartEntity.addPart("multipartFile", inputStreamBody).setCharset(Charset.forName("utf-8"));
        HttpEntity httpEntity = multipartEntity.build();
        httpPost.setEntity(httpEntity);

        // 执行http请求
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse httpResponse = null;
        try {
            boolean successFlag = true;
            String msg = "";
            String responseStr = "";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Consts.UTF_8);
                    System.out.println(responseStr);
                }
            } else {
                msg = "Invoke UniuserAccountAuditInfoSrv service failed. the http status code is " + statusCode;
                successFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }

}
