package za.co.invoke.solutions.naturalgasprices.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:4200")
                .allowedMethods("")
                .allowedOriginPatterns("*")
                .allowedHeaders("Authorization")
                .exposedHeaders("Authorization")
//                .allowCredentials(true)
                .maxAge(3600).allowedHeaders("Authorization").allowCredentials(true);
    }
}
