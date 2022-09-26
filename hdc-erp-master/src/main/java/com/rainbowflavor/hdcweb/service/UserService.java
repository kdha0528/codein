package com.rainbowflavor.hdcweb.service;

import com.rainbowflavor.hdcweb.domain.*;
import com.rainbowflavor.hdcweb.dto.SignupDto;
import com.rainbowflavor.hdcweb.mapstruct.sign.SignupMapper;
import com.rainbowflavor.hdcweb.repository.JpaRoleRepository;
import com.rainbowflavor.hdcweb.repository.JpaUserConfirmTokenRepository;
import com.rainbowflavor.hdcweb.repository.JpaUserRepository;
import com.rainbowflavor.hdcweb.repository.JpaUserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserConfirmTokenService userConfirmTokenService;
    private final JpaUserConfirmTokenRepository userConfirmTokenRepository;
    private final JpaRoleRepository roleRepository;
    private final JpaUserRepository userRepository;
    private final JpaUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignupMapper signupMapper = Mappers.getMapper(SignupMapper.class);


    @Transactional
    public SignupDto joinUser(SignupDto signupDto) {
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        User user = signupMapper.toEntity(signupDto);

        User saveUser = userRepository.save(user);
        Role findRole = roleRepository.findByName(ERole.ROLE_ADMIN);

        UserRole userRole = new UserRole();
        userRole.setUser(saveUser);
        userRole.setRole(findRole);

        userConfirmTokenService.createEmailConfirmToken(saveUser.getId());
        userRoleRepository.save(userRole);

        return signupMapper.toDto(saveUser);
    }

    public User findUser(Long userId) {
        return userRepository.getOne(userId);
    }

    public User findUser(String name) {
        Optional<User> findByName = userRepository.findByName(name);
        User user = findByName.get();
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUser = userRepository.findByUsername(username);
        User user = findUser.orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> collect = user.getUserRoles().stream().map(userRole ->
                new SimpleGrantedAuthority(userRole.getRole().getName().name())).collect(Collectors.toList());

        boolean enable = !user.isEmailVerify();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .disabled(enable)
                .password(user.getPassword())
                .authorities(collect)
                .build();
    }

    @Transactional
    public void confirmEmail(String token) {
        UserConfirmToken findConfirmationToken = userConfirmTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        User findUser = findUser(findConfirmationToken.getUserId());
        findConfirmationToken.useToken();
        findUser.setEmailVerify(true);
        userConfirmTokenRepository.save(findConfirmationToken);
        userRepository.save(findUser);
    }
}
