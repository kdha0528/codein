package com.codein.config;

import com.codein.repository.tokens.TokensRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(tokensRepository));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(tokensRepository, memberRepository, notificationService))
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/favicon.ico"); // 인증없이 접근 가능하게
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = "file:///C:/workspace/springboot/codein/images/profile/";
        registry.addResourceHandler("/images/profile/**")
                .addResourceLocations(resourceLocation)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:5173", "https://codein.loca.lt", "https://code-in.site")
                .allowedHeaders("Origin, X-Requested-With, Content-Type, Accept, Authorization")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .maxAge(1800);
    }
}
