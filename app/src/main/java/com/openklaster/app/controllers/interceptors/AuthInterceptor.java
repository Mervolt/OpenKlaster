package com.openklaster.app.controllers.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO INJECT IT SOMEWHERE
// TODO zaimplementowac autentykacje tutaj
public class AuthInterceptor implements HandlerInterceptor {

    //
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("NOPE");
        return false;
    }

}
