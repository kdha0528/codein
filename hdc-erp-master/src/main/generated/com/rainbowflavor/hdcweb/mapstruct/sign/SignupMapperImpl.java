package com.rainbowflavor.hdcweb.mapstruct.sign;

import com.rainbowflavor.hdcweb.domain.User;
import com.rainbowflavor.hdcweb.dto.SignupDto;
import java.util.Date;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-06T17:01:57+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_292 (AdoptOpenJDK)"
)
@Component
public class SignupMapperImpl implements SignupMapper {

    @Override
    public SignupDto toDto(User arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SignupDto signupDto = new SignupDto();

        return signupDto;
    }

    @Override
    public User toEntity(SignupDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        String name = null;
        String username = null;
        String password = null;
        String email = null;
        String position = null;
        String phone = null;
        String address = null;
        Date birth = null;

        User user = new User( name, username, password, email, position, phone, address, birth );

        return user;
    }

    @Override
    public void updateFromDto(SignupDto arg0, User arg1) {
        if ( arg0 == null ) {
            return;
        }
    }
}
