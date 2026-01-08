package restaurantclient.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // DEBUG: Vedem dacă intră aici
        System.out.println(">>> INTERCEPTOR: Verific cererea către: " + request.getRequestURI());

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("userLogat") != null) {
            System.out.println(">>> INTERCEPTOR: User logat! Permite accesul.");
            return true;
        }

        System.out.println(">>> INTERCEPTOR: Nu e logat! Redirect la Login.");
        response.sendRedirect("/login");
        return false;
    }
}