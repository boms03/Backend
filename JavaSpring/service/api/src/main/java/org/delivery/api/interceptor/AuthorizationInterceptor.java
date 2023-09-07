package org.delivery.api.interceptor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());
        // chrome: OPTION (skip)
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }
        //resource js,html,png
        //handler is the class object of controller object
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        // header validation

        return true;
    }
}