package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.follow.Follow;
import com.codein.domain.member.Member;
import com.codein.domain.member.ProfileEditor;
import com.codein.error.exception.auth.InvalidTokensCookieException;
import com.codein.error.exception.auth.RefreshTokenNullException;
import com.codein.error.exception.member.*;
import com.codein.repository.tokens.TokensRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.member.follow.FollowRepository;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestservicedto.member.*;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokensRepository tokensRepository;
    private final FollowRepository followRepository;
    private final S3Service s3Service;


    @Transactional
    public Member signup(SignupServiceDto signupServiceDto) {

        if (memberRepository.findByEmail(signupServiceDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException();
        } else if (memberRepository.findByPhone(signupServiceDto.getPhone()) != null) {
            throw new PhoneAlreadyExistsException();
        } else if (memberRepository.findByNickname(signupServiceDto.getNickname()) != null) {
            throw new NicknameAlreadyExistsException();
        }
        Member member = signupServiceDto.toEntity();
        memberRepository.save(member);
        return member;
    }


    @Transactional
    public ArrayList<String> login(LoginServiceDto loginServiceDto) {

        Member member = memberRepository.findByEmail(loginServiceDto.getEmail());

        if (member == null) {
            throw new MismatchLoginInputException();
        }

        if (!passwordEncoder.matches(loginServiceDto.getPassword(), member.getPassword())) {
            throw new MismatchLoginInputException();
        }

        Tokens token = Tokens.builder()
                .member(member)
                .build();

        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(token.getRefreshToken());
        tokens.add(token.getAccessToken());
        return tokens;
    }

    @Transactional
    public void logout(Cookie cookie) {
        if(cookie.getName().equals("accesstoken")) {
            Tokens tokens = tokensRepository.findByAccessToken(cookie.getValue())
                    .orElseThrow(MemberNotLoginException::new);
            tokensRepository.delete(tokens);
        } else if (cookie.getName().equals("refreshtoken")) {
            Tokens tokens = tokensRepository.findByRefreshToken(cookie.getValue());
            if(tokens != null) {
                tokensRepository.delete(tokens);
            } else {
                throw new RefreshTokenNullException();
            }
        }else{
            throw new InvalidTokensCookieException();
        }
    }

    @Transactional
    public void changePassword(String accessToken, PasswordServiceDto passwordServiceDto) {
        Tokens tokens = tokensRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = tokens.getMember();

        if (!passwordEncoder.matches(passwordServiceDto.getOriginPassword(), member.getPassword())) {
            throw new MismatchLoginInputException();
        }

        member.setPassword(passwordEncoder.encrypt(passwordServiceDto.getNewPassword()));
    }

    @Transactional
    public void changeEmail(String accessToken, EmailServiceDto emailServiceDto) {
        Tokens tokens = tokensRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = tokens.getMember();

        Member emailMember = memberRepository.findByEmail(emailServiceDto.getEmail());
        if (emailMember != null && !Objects.equals(emailMember.getEmail(), member.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        member.setEmail(emailServiceDto.getEmail());
    }

    @Transactional
    public void changePhone(String accessToken, PhoneServiceDto phoneServiceDto) {
        Tokens tokens = tokensRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = tokens.getMember();

        Member phoneMember = memberRepository.findByPhone(phoneServiceDto.getPhone());
        if (phoneMember != null && !Objects.equals(phoneMember.getEmail(), member.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        member.setPhone(phoneServiceDto.getPhone());
    }


    @Transactional
    public void editProfile(String accessToken, EditProfileServiceDto editProfileServiceDto) throws IOException {
        Tokens tokens = tokensRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = tokens.getMember();

        if (editProfileServiceDto.getNickname() != null) {  // 닉네임 중복 검사
            Member nicknameMember = memberRepository.findByNickname(editProfileServiceDto.getNickname());
            if (nicknameMember != null && !Objects.equals(nicknameMember.getNickname(), member.getNickname())) {
                throw new NicknameAlreadyExistsException();
            }
        }

        ProfileEditor.ProfileEditorBuilder profileEditorBuilder = member.toProfileEditor();
        String imgFileName = s3Service.uploadProfileImage(editProfileServiceDto.getProfileImage());
        ProfileEditor profileEditor = profileEditorBuilder
                .name(editProfileServiceDto.getName())
                .nickname(editProfileServiceDto.getNickname())
                .imgFileName(imgFileName)
                .build();
        if (member.getProfileImage() != null) {
            s3Service.removeProfileImage(member.getProfileImage().getImgFileName());
        }
        member.editProfile(profileEditor);
    }


    @Transactional
    public void deleteMember(String accessToken) {
        Member member = memberRepository.findByAccessToken(accessToken);

        if(member != null){
            member.deleteMember();
        } else {
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public ArrayList<Member> createMemberDummies() {
        Random random = new Random();
        ArrayList<Member> memberList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            SignupDto signupDto = SignupDto.builder()
                    .name("데일이")
                    .email("kdha"+ i + "@gmail.com")
                    .nickname(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
                    .password(UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                    .phone("010" + (random.nextInt(90000000) + 10000000))
                    .birth("1996-05-28")
                    .sex("male")
                    .build();
            Member member = signup(signupDto.toSignupServiceDto());
            memberList.add(member);
        }
        return memberList;
    }

    @Transactional
    public Follow followMember(FollowServiceDto followServiceDto){
        Member follower = memberRepository.findByAccessToken(followServiceDto.getAccessToken());
        Member following = memberRepository.findById(followServiceDto.getFollowingId())
                .orElseThrow(MemberNotExistsException::new);

        if (follower != null) {
            Follow exists = followRepository.findByMembers(follower,following);

            if(exists == null){
                Follow follow = Follow.builder()
                        .follower(follower)
                        .following(following)
                        .build();
                followRepository.save(follow);
                return follow;
            } else {
                followRepository.delete(exists);
                return null;
            }
        } else {
            throw new MemberNotExistsException();
        }
    }
}
