package com.monk.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 官网示例:https://docs.spring.io/spring-cloud-netflix/docs/2.2.6.RELEASE/reference/html/#zuul-developer-guide-enable-filters
 */
@Component
public class LogFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public String filterType() {
        /**
         * 有四种类型:
         * 正常的调用链: pre  >>  路由前  >>  route  >>  调用源服务  >>  post
         * 异常的调用链: error
         */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        /**
         * FilterConstants.PRE_DECORATION_FILTER_ORDER 是zuul自己写的一个默认过滤器
         * 在zuul的pre过滤器类型执行链路上的位置,在PreDecorationFilter这个过滤中,zuul执行了
         * 一系列的操作:比方说将requestAttribute这些对象放到了RequestContext中
         *
         * 数字越小优先级越高,用于排序相同类型过滤器的执行顺序
         *
         * 实际开发中,根据实际的业务需求来确定自己写的filter的执行顺序,需要在PreDecorationFilter之前
         * 还是之后执行,可以通过FilterConstants.PRE_DECORATION_FILTER_ORDER -1 or + 1来实现,
         * 提供代码的可读性
         */
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        // false表示不启动当前这个过滤器,true表示启用,默认false
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Object obj = ctx.get(FilterConstants.REQUEST_URI_KEY);
        String requestUri = null;
        if(null != obj){
            requestUri = obj.toString();
        }
        logger.info("{}访问了{}, 路由后的地址[{}]", request.getRemoteAddr(), request.getRequestURI(), requestUri);
        return null;
    }
}
