package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.domain.MemberEditor;
import com.codein.domain.Session;
import com.codein.error.exception.member.*;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestservicedto.EditMemberServiceDto;
import com.codein.requestservicedto.LoginServiceDto;
import com.codein.responsedto.LoginResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;

    @Transactional
    public void signup(Member member) {

        if (memberRepository.findByEmail(member.getEmail()) != null) {
            throw new EmailAlreadyExistsException();
        } else if (memberRepository.findByPhone(member.getPhone()) != null) {
            throw new PhoneAlreadyExistsException();
        } else if (memberRepository.findByNickname(member.getNickname()) != null) {
            throw new NicknameAlreadyExistsException();
        }
        member.encryptPassword(passwordEncoder.encrypt(member.getPassword()));
        memberRepository.save(member);
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
    public void deleteMember(String accessToken) {
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = session.getMember();
        memberRepository.delete(member);
    }

}
