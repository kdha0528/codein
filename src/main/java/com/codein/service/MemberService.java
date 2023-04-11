package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Session;
import com.codein.domain.image.ProfileImage;
import com.codein.domain.member.Member;
import com.codein.domain.member.MemberEditor;
import com.codein.domain.member.ProfileEditor;
import com.codein.error.exception.member.*;
import com.codein.error.exception.profileimage.ImageTooLargeException;
import com.codein.error.exception.profileimage.InvalidImageException;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.profileimage.ProfileImageRepository;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestservicedto.member.*;
import com.codein.responsedto.LoginResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;
    private final ProfileImageRepository profileImageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public void signup(SignupServiceDto signupServiceDto) {

        if (memberRepository.findByEmail(signupServiceDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException();
        } else if (memberRepository.findByPhone(signupServiceDto.getPhone()) != null) {
            throw new PhoneAlreadyExistsException();
        } else if (memberRepository.findByNickname(signupServiceDto.getNickname()) != null) {
            throw new NicknameAlreadyExistsException();
        }

        memberRepository.save(signupServiceDto.toEntity());
    }


    @Transactional
    public String login(LoginServiceDto loginServiceDto) {

        Member member = memberRepository.findByEmail(loginServiceDto.getEmail());

        if (member == null) {
            throw new InvalidLoginInputException();
        }

        if (!passwordEncoder.matches(loginServiceDto.getPassword(), member.getPassword())) {
            throw new InvalidLoginInputException();
        }

        Session session = Session.builder()
                .member(member)
                .build();

        return session.getAccessToken();
    }

    @Transactional
    public ResponseCookie buildResponseCookie(String token) {
        return ResponseCookie.from("accesstoken", token)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofMinutes(30))
                .sameSite("Strict")
                .build();
    }


    public List<LoginResponseDto> getMemberList(PageSizeDto pageSizeDto) {
        return memberRepository.getMemberResponseList(pageSizeDto);
    }

    @Transactional
    public void logout(String accessToken) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        sessionRepository.delete(session);
    }

    @Transactional
    public void editMember(String accessToken, EditMemberServiceDto editMemberServiceDto) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        if (editMemberServiceDto.getEmail() != null) {
            Member emailMember = memberRepository.findByEmail(editMemberServiceDto.getEmail());
            if (emailMember != null && !Objects.equals(emailMember.getEmail(), member.getEmail())) {
                throw new EmailAlreadyExistsException();
            }
        }
        if (editMemberServiceDto.getPhone() != null) {
            Member phoneMember = memberRepository.findByPhone(editMemberServiceDto.getPhone());
            if (phoneMember != null && !Objects.equals(phoneMember.getPhone(), member.getPhone())) {
                throw new PhoneAlreadyExistsException();
            }
        }
        if (editMemberServiceDto.getNickname() != null) {
            Member nicknameMember = memberRepository.findByNickname(editMemberServiceDto.getNickname());
            if (nicknameMember != null && !Objects.equals(nicknameMember.getNickname(), member.getNickname())) {
                throw new NicknameAlreadyExistsException();
            }
        }

        String encryptedPassword = null;
        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        if (editMemberServiceDto.getPassword() != null) {
            encryptedPassword = passwordEncoder.encrypt(editMemberServiceDto.getPassword());
        }

        MemberEditor memberEditor = memberEditorBuilder
                .email(editMemberServiceDto.getEmail())
                .password(encryptedPassword)
                .name(editMemberServiceDto.getName())
                .nickname(editMemberServiceDto.getNickname())
                .phone(editMemberServiceDto.getPhone())
                .build();

        member.edit(memberEditor);
    }

    @Transactional
    public void changePassword(String accessToken, PasswordServiceDto passwordServiceDto) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        member.setPassword(passwordEncoder.encrypt(passwordServiceDto.getPassword()));
    }

    @Transactional
    public void changeEmail(String accessToken, EmailServiceDto emailServiceDto) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        Member emailMember = memberRepository.findByEmail(emailServiceDto.getEmail());
        if (emailMember != null && !Objects.equals(emailMember.getEmail(), member.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        member.setEmail(emailServiceDto.getEmail());
    }

    @Transactional
    public void changePhone(String accessToken, PhoneServiceDto phoneServiceDto) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        Member phoneMember = memberRepository.findByPhone(phoneServiceDto.getPhone());
        if (phoneMember != null && !Objects.equals(phoneMember.getEmail(), member.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        member.setPhone(phoneServiceDto.getPhone());
    }


    @Transactional
    public void editProfile(String accessToken, EditProfileServiceDto editProfileServiceDto) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        if (editProfileServiceDto.getNickname() != null) {  // 닉네임 중복 검사
            Member nicknameMember = memberRepository.findByNickname(editProfileServiceDto.getNickname());
            if (nicknameMember != null && !Objects.equals(nicknameMember.getNickname(), member.getNickname())) {
                throw new NicknameAlreadyExistsException();
            }
        }

        ProfileEditor.ProfileEditorBuilder profileEditorBuilder = member.toProfileEditor();
        String imgFileName = saveProfileImage(editProfileServiceDto.getProfileImage());
        ProfileEditor profileEditor = profileEditorBuilder
                .name(editProfileServiceDto.getName())
                .nickname(editProfileServiceDto.getNickname())
                .imgFileName(imgFileName)
                .build();

        removeProfileImage(member.getProfileImage());
        member.editProfile(profileEditor);
    }

    @Transactional
    public void removeProfileImage(ProfileImage profileImage) {
        if (profileImage != null) {
            File file = new File(uploadPath + "\\" + profileImage.getImgFileName());
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("파일 삭제 성공");
                } else {
                    System.out.println("파일 삭제 실패");
                }
            }
        }
    }

    @Transactional
    public String saveProfileImage(MultipartFile file) {
        if (file == null) return null; // 이미지 변경 없을 시 return null

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new InvalidImageException();  // 이미지파일 아닐 시 EXCEPTION
        }

        if (file.getSize() > 1000000) throw new ImageTooLargeException(); // 사진 크기가 1MB 넘어가면 Exception

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        Path imageFilePath = Paths.get(uploadPath + imageFileName);

        try {
            Files.write(imageFilePath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFileName;
    }

    @Transactional
    public void deleteMember(String accessToken) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = session.getMember();
        removeProfileImage(member.getProfileImage());
        memberRepository.delete(member);
    }

}
