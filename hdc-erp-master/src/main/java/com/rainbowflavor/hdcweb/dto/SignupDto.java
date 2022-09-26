package com.rainbowflavor.hdcweb.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SignupDto {
    private String name;
    private String username;
    private String password;
    private String email;
    private String position;
    private String phone;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
}
