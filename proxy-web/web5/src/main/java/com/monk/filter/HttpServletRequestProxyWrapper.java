package com.monk.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

public class HttpServletRequestProxyWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    /**
     * 构造方法
     * 
     * @param request
     *            请求对象
     * @throws IOException
     *             IO异常对象
     * @author Monk
     * @date 2021年7月21日 上午10:09:24
     */
    public HttpServletRequestProxyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = readBytes(request.getReader(), StandardCharsets.UTF_8.name());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    /**
     * 将请求中的InputStream读成字节数组
     * 
     * @param br
     *            InputStream流
     * @param encoding
     *            编码格式
     * @return 字节数组
     * @throws IOException
     *             IO异常对象
     * @author Monk
     * @date 2021年7月21日 上午10:07:16
     */
    private byte[] readBytes(BufferedReader br, String encoding) throws IOException {
        String str = null, retStr = "";
        while ((str = br.readLine()) != null) {
            retStr += str + "\r\n";
        }
        if (StringUtils.isNotBlank(retStr)) {
            return retStr.getBytes(Charset.forName(encoding));
        }
        return new byte[0];
    }

}
