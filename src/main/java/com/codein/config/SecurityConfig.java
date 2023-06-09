package com.codein.config;


import com.codein.domain.member.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class SecurityConfig {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySecured {
        Role role() default Role.MEMBER;
    }
}
