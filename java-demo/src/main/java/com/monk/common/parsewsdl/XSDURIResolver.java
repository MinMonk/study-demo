package com.monk.common.parsewsdl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.apache.ws.commons.schema.resolver.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * Schema Collection 地址解析器
 * 实现URIResolver接口，xsd 资源加载
 * @author houyongchuan
 */
public class XSDURIResolver implements URIResolver {
	private static final Logger logger = LoggerFactory.getLogger(XSDURIResolver.class);
	private String userName;
	private String passwd;
	private int httpResponseCode;
	private String httpResponseMessage;
	private String collectionBaseURI;
	private int maxContentLength = 1024*1024*8;
	@Override
	public InputSource resolveEntity(String targetNamespace, String schemaLocation, String baseUri) {
		 if (baseUri != null) {
			 URL url = null;
	            try {
	                File baseFile = null;
	                try {
	                    URI uri = new URI(baseUri);
	                    baseFile = new File(uri);
	                    if (!baseFile.exists()) {
	                        baseFile = new File(baseUri);
	                    }
	                } catch (Throwable ex) {
	                    baseFile = new File(baseUri);
	                }
	                if (baseFile.exists()) {
	                    baseUri = baseFile.toURI().toString();
	                } else if (collectionBaseURI != null) {
	                    baseFile = new File(collectionBaseURI);
	                    if (baseFile.exists()) {
	                        baseUri = baseFile.toURI().toString();
	                    }
	                }
	                url = new URL(new URL(baseUri), schemaLocation);
	                InputStream content = getContentAsInputStream(url);
	                return new InputSource(content);
	            } catch (Exception e) {
	            	logger.error("collectionBaseURI:"+collectionBaseURI+";baseUri:"+baseUri+";schemaLocation:"+schemaLocation);
					throw new RuntimeException("Unable to resolve imported document at '" + url
							+ (baseUri == null ? "'." : "', relative to '" + baseUri + "'."), e);
				}
	        }
	        return new InputSource(schemaLocation);
	}
	
	
	/**
	 * 获取wsdl url 资源输入流
	 * 
	 * @param url
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public InputStream getContentAsInputStream(URL url)
			throws SecurityException, IllegalArgumentException, IOException {
		if (url == null) {
			throw new IllegalArgumentException("URL cannot be null.");
		}

		try {
			String author = null;
			// 如果有设置用户密码，计算认证信息
			if (userName != null && passwd != null) {
				author = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + passwd).getBytes());
			}
			
			URLConnection conn = url.openConnection();
			// 如果有认证信息，则添加httpbasic认证信息
			if (author != null) {
				logger.info("add http basic authorization info in URL RequestProperty,URL[{}]",url);
				conn.setRequestProperty("Authorization", author);
			}
			// 设置超时
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			ByteArrayOutputStream baos = null;
			InputStream inputStream = null;
			byte[] content = null;
			HttpURLConnection httpConn = null;
			HttpURLConnection httpRedirectConn = null;
			if (conn instanceof HttpURLConnection) {
				httpConn = (HttpURLConnection) conn;
			}
			
			// 获取输入流
			try {
				if(httpConn!=null&&httpConn.getResponseCode()==200) {
					inputStream = conn.getInputStream();
				}else if(httpConn!=null&&(httpConn.getResponseCode()==301||httpConn.getResponseCode()==302)) {
					//重定向，单次重定向，多次不考虑
					//得到重定向的地址
					String location = httpConn.getHeaderField("Location");
					logger.info("Request URL[{}] got response: {}" ,url, conn.getHeaderField(0));
					logger.info("Redirect url: {} ",location);
			        URL u1 = new URL(location);
			        httpRedirectConn = (HttpURLConnection) u1.openConnection();
			        // 如果有认证信息，则添加httpbasic认证信息
			        if (author != null) {
						logger.info("add http basic authorization info in URL RequestProperty,URL[{}]",url);
						httpRedirectConn.setRequestProperty("Authorization", author);
					}
			        httpRedirectConn.setConnectTimeout(10000);
			        httpRedirectConn.setReadTimeout(30000);
					inputStream = httpRedirectConn.getInputStream();
				}else if(httpConn!=null) {
					throw new IOException("Request URL["+url+"] got response: " + conn.getHeaderField(0));
				}else {
					inputStream = conn.getInputStream();
				}
				
				//读取内容到缓存中
				int readedContentLength = 0;
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int len;
				while ((len = inputStream.read(buffer)) > -1 ) {
				     baos.write(buffer, 0, len);
				     readedContentLength += len;
				     if(readedContentLength > maxContentLength) {
				    	 throw new IOException(" Over Max Content Length limit:"+maxContentLength);
				     }
				}
				baos.flush();
				content = baos.toByteArray();
				//关闭流
				inputStream.close();
				baos.close();
				baos = null;
				inputStream = null;
			} catch (IOException e) {
				if (conn instanceof HttpURLConnection) {
					httpConn = (HttpURLConnection) conn;
					httpResponseCode = httpConn.getResponseCode();
					httpResponseMessage = httpConn.getResponseMessage();
					//关闭http连接
					httpConn.disconnect();
					logger.error("Request URL["+url+"] got response: " + conn.getHeaderField(0));
				}
				throw e;
			}finally {
				if (conn!=null&&conn instanceof HttpURLConnection) {
					httpConn = (HttpURLConnection) conn;
					httpResponseCode = httpConn.getResponseCode();
					httpResponseMessage = httpConn.getResponseMessage();
					//关闭http连接
					httpConn.disconnect();
					logger.error("Request URL["+url+"] got response: " + conn.getHeaderField(0));
				}
				
				if(httpRedirectConn!=null) {
					httpRedirectConn.disconnect();
				}
				
				if(inputStream!=null) {
					inputStream.close();
				}
			}

			if (content == null) {
				throw new IllegalArgumentException("No content.");
			}
			if(logger.isDebugEnabled()) {
				logger.debug(new String(content));
			}
			return new ByteArrayInputStream(content);
		} catch (SecurityException e) {
			throw new SecurityException("Your JVM's SecurityManager has " + "disallowed this.");
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("This file was not found: " + url);
		}
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getHttpResponseCode() {
		return httpResponseCode;
	}
	public void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
	}
	public String getHttpResponseMessage() {
		return httpResponseMessage;
	}
	public void setHttpResponseMessage(String httpResponseMessage) {
		this.httpResponseMessage = httpResponseMessage;
	}


	public String getCollectionBaseURI() {
		return collectionBaseURI;
	}


	public void setCollectionBaseURI(String collectionBaseURI) {
		this.collectionBaseURI = collectionBaseURI;
	}

}
