package com.tce.oa.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/12/3 16:57
 **/
public class HttpServletUtil {

    /**
     * 获取 HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
}
