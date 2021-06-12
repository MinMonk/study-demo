package com.vispractice.soa.license.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.monk.license.LicenseVerify;
import com.monk.license.cache.LicenseCache;
import com.monk.license.cache.LicenseInfo;
import com.monk.license.cache.LicenseStatu;

/**
 * License证书过滤器
 *
 * @author Monk
 * @version V1.0
 * @date 2021年5月20日 下午13:14:00
 */
public class LicenseVerifyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        LicenseInfo info = LicenseCache.getInstance().getLicenseInfo();
        if (null == info.getStatu()) {
            LicenseVerify licenseVerify = getLicenseVerify();
            licenseVerify.verify();
        }

        if (LicenseStatu.LOCK.equals(info.getStatu())) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.print("{\"status\":\"SOA-400\",\"msg\":\"非法篡改系统时间，系统已锁定，请联系远行科技相关负责人。\"}");
            pw.flush();
            pw.close();
            return;
        } else if (LicenseStatu.INVALID.equals(info.getStatu())) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.print("{\"status\":\"SOA-401\",\"msg\":\"证书过期，请联系远行科技相关负责人重新申请证书！\"}");
            pw.flush();
            pw.close();
            return;
        } else {
            if(System.currentTimeMillis() > info.getExipreDate()) {
                // 过期后，修改缓存中的校验时间，防止篡改系统时间而缓存没有失效导致任然可以继续访问
                info.setValidateTime(System.currentTimeMillis());
                info.setStatu(LicenseStatu.INVALID);
                
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.print("{\"status\":\"SOA-401\",\"msg\":\"证书过期，请联系远行科技相关负责人重新申请证书！\"}");
                pw.flush();
                pw.close();
                return;
            }else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        // 清空license缓存信息
        LicenseCache.clearCache();
    }

    /**
     * 获取Spring容器中LicenseVerify的实例化对象
     * 
     * @return LicenseVerify的实例化对象
     * @author Monk
     * @date 2021年5月22日 下午4:00:02
     */
    private LicenseVerify getLicenseVerify() {
        WebApplicationContext wac = getWebApplicationContext();
        LicenseVerify licenseVerify = null;
        if (null != wac) {
            licenseVerify = wac.getBean(LicenseVerify.class);
        }
        return licenseVerify;
    }

    /**
     * 获取WebApplicationContext上下文对象
     * 
     * @return 上下文对象
     * @date 2021年5月20日 下午13:14:00
     */
    private WebApplicationContext getWebApplicationContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }
}
