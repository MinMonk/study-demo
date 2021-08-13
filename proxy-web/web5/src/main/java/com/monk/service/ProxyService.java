package com.monk.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProxyService {

    void proxy(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException, ServletException;

}
