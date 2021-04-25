package com.example.demo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class LogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long requestTime = System.currentTimeMillis();
        request = new RequestWrapper(request);
        response = new ResponseWrapper(response);
        Map<String, Object> map = new HashMap<String, Object>();

        String uri = request.getRequestURI();

        map.put("URI", uri);
        map.put("Method", request.getMethod());
        map.put("Protocol",request.getProtocol());

        // 获取HEADER参数
        List<Map<String,String>> headerList = new ArrayList<>();
        Map<String,String> headerMaps = new HashMap<String,String>();
        for(Enumeration<String> enu = request.getHeaderNames(); enu.hasMoreElements();){
            String name = enu.nextElement();
            headerMaps.put(name,request.getHeader(name));
        }
        headerList.add(headerMaps);
        map.put("headers", headerList);

        List<Map<String,String>> parameterList = new ArrayList<>();
        Map<String,String> parameterMaps = new HashMap<String,String>();
        for(Enumeration<String> names = request.getParameterNames();names.hasMoreElements();){
            String name = names.nextElement();
            parameterMaps.put(name, request.getParameter(name));
        }
        parameterList.add(parameterMaps);
        map.put("parameters", parameterList);

        //静态资源 跳过
        if (uri.contains(".")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 输出请求体
        String requestBody = "";
        String requestType = request.getHeader(HttpHeaders.CONTENT_TYPE);

        if (requestType != null) {
            // xml json
            if (requestType.startsWith(MediaType.APPLICATION_JSON_VALUE) || requestType.startsWith(MediaType.APPLICATION_XML_VALUE)) {
                requestBody = getRequestBody(request);
            // 普通表单提交
            } else if (requestType.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                requestBody = toJson(request.getParameterMap());
            // 文件表单提交
            } else if (requestType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                requestBody = getFormParam(request);
            }
        }
        map.put("requestType", requestType);
        map.put("requestBody", requestBody);

        filterChain.doFilter(request, response);

        String responseBody = new String(((ResponseWrapper) response).toByteArray(), "UTF-8");
        String responseType = response.getContentType();
        map.put("responseType", responseType);
        map.put("responseBody", responseBody);

        long costTime = System.currentTimeMillis() - requestTime;
        map.put("total time", costTime);
        logger.info("request and response parameter : [" + JSON.toJSONString(map) + "]");
    }

    private String getRequestBody(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            return "";
        }
        try {
            return IOUtils.toString(request.getReader());
        } catch (IOException e) {
            log.error("获取请求体失败", e);
            return "";
        }
    }

    private String getFormParam(HttpServletRequest request) {
        MultipartResolver resolver = new StandardServletMultipartResolver();
        MultipartHttpServletRequest mRequest = resolver.resolveMultipart(request);

        Map<String, Object> param = new HashMap<>();
        Map<String, String[]> parameterMap = mRequest.getParameterMap();
        if (!parameterMap.isEmpty()) {
            param.putAll(parameterMap);
        }
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        if (!fileMap.isEmpty()) {
            for (Map.Entry<String, MultipartFile> fileEntry : fileMap.entrySet()) {
                MultipartFile file = fileEntry.getValue();
                param.put(fileEntry.getKey(), file.getOriginalFilename() + "(" + file.getSize() + " byte)");
            }
        }
        return toJson(param);
    }

    private static String toJson(Object object) {
        return JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
    }
}
