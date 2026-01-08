
package restaurantclient.config;

import org.springframework.context.annotation.Configuration; // <--- Importul acesta trebuie să existe
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // <--- FĂRĂ ASTA NU MERGE NIMIC! Verifică să fie aici.
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(">>> WEBCONFIG: Se înregistrează interceptorul..."); // Mesaj de debug
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/register", "/css/**", "/js/**");
    }
}