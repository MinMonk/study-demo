/**
 * 
 * 文件名：Test.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.webservice.restful;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年9月11日 下午5:20:34
 */
public class Test {

    private static void httpURLPOSTCase(String methodUrl, String requestParam) {
        HttpURLConnection connection = null;
        OutputStream dataout = null;
        BufferedReader reader = null;
        String line = null;
        try {
            URL url = new URL(methodUrl);
            connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
            connection.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
            connection.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
            connection.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
            connection.setUseCaches(false);// post请求不能使用缓存设为false
            connection.setConnectTimeout(3000);// 连接主机的超时时间
            connection.setReadTimeout(3000);// 从主机读取数据的超时时间
            connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
            connection.setRequestProperty("charset", "utf-8");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

            dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
            dataout.write(requestParam.getBytes());
            dataout.flush();
            dataout.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));//
                }
                System.out.println(result.toString());
                JSONObject jsonObj = JSONObject.fromObject(result.toString());
                Iterator it = jsonObj.keys();
                Map<String, Object> map = new HashMap<String, Object>();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    map.put(key, jsonObj.get(key));
                }
                System.out.println(map.get("data"));
                System.out.println(map.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
    }
    
    
    public static void main(String[] args) {
//        httpURLPOSTCase("http://1zm8875695.iok.la:36271/iotview/IOT_CDP_C01_AuthLogin");
        String body = "{\r\n" + 
                "    \"userName\": \"admin\",\r\n" + 
                "    \"password\": \"123456\",\r\n" + 
                "    \"type\": \"6\",\r\n" + 
                "    \"sign\": \"897b6add0b079f16070c1c1fb86ad782\",\r\n" + 
                "    \"destURL\": \"index.do\"\r\n" + 
                "}";
        httpURLPOSTCase("http://10.204.103.243:8088/authLogin", body);
        /*String str = "{\r\n" + 
                "    \"userName\": \"admin\",\r\n" + 
                "    \"password\": \"123456\",\r\n" + 
                "    \"type\": \"6\",\r\n" + 
                "    \"sign\": \"897b6add0b079f16070c1c1fb86ad782\",\r\n" + 
                "    \"destURL\": \"index.do\"\r\n" + 
                "}";
        JSONObject jsonObj = JSONObject.fromObject(str);
        
        String result = httpPost("http://1zm8875695.iok.la:36271/iotview/IOT_CDP_C01_AuthLogin", jsonObj, null);
        System.out.println(result);*/
    }

    public static String httpPost(String url, JSONObject jsonParam, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 请求返回结果
        Map<String, Object> jsonResult = new HashMap<>(16);
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000).build();
        httpPost.setConfig(config);
        String strResult = "";
        try {
            // 设置参数解决中文乱码
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            // 添加Header
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            // 发送请求
            CloseableHttpResponse result = httpClient.execute(httpPost);
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    // 读取服务器返回的json数据（然后解析）
                    strResult = EntityUtils.toString(result.getEntity(), "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return strResult;
    }
}
