package com.vn.cinema_internal_java_spring_rest.config.permissionDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/storage/**",
                "/api/v1/films/**", "/api/v1/categories/**", "/api/v1/times/**", "/api/v1/files",
                "/api/v1/shows/**", "/api/v1/seats/**", "/api/v1/bills/**", "/api/v1/histories/**",
                "/api/v1/names/**", "/api/v1/email/**"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
