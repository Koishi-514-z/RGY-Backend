package org.example.rgybackend.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        //     return true;
        // }
        // HttpSession session = request.getSession(false);
        // if(session == null || session.getAttribute("user") == null) {
        //     response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //     response.setContentType("application/json");
        //     response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"code\":401}");
        //     return false;
        // }
        return true;
    }
}
