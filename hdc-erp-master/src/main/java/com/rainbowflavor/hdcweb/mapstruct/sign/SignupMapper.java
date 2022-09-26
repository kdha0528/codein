package com.rainbowflavor.hdcweb.mapstruct.sign;

import com.rainbowflavor.hdcweb.domain.User;
import com.rainbowflavor.hdcweb.dto.SignupDto;
import com.rainbowflavor.hdcweb.mapstruct.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//componentModel 속성을 안주면 spring bean 으로 등록되지 않음
@Mapper(componentModel = "spring")
public interface SignupMapper extends GenericMapper<SignupDto, User> {
}
