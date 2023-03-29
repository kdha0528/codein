package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.domain.MemberEditor;
import com.codein.domain.Session;
import com.codein.error.exception.EmailAlreadyExistsException;
import com.codein.error.exception.InvalidLoginInputException;
import com.codein.error.exception.MemberNotLoginException;
import com.codein.error.exception.PhoneAlreadyExistsException;
import com.codein.repository.MemberRepository;
import com.codein.repository.SessionRepository;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestservicedto.LoginServiceDto;
import com.codein.requestservicedto.MemberEditServiceDto;
import com.codein.responsedto.MemberResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;


    @Transactional
    public void signup(Member member) {

        Member emailMember = memberRepository.findByEmail(member.getEmail());
        Member phoneMember = memberRepository.findByPhone(member.getPhone());

        if (emailMember != null) {
            throw new EmailAlreadyExistsException();
        } else if (phoneMember != null) {
            throw new PhoneAlreadyExistsException();
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
        member.addSession(session);

        System.out.println("member get session = " + member.getSessions().get(0).getAccessToken());
        System.out.println("session get member role =" + session.getMember().getRole());
        return session.getAccessToken();
    }

    @Transactional
    public ResponseCookie createResponseCookie(String accessToken) {
        return ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();
    }


    public List<MemberResponseDto> getMemberList(PageSizeDto pageSizeDto) {
        return memberRepository.getMemberResponseList(pageSizeDto);
    }

    @Transactional
    public void logout(String accessToken) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();
        member.deleteSession(session);
        sessionRepository.save(session);
    }

    @Transactional
    public void memberEdit(String accessToken, MemberEditServiceDto memberEditServiceDto) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        String encryptedPassword = null;
        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        if (memberEditServiceDto.getPassword() != null) {
            encryptedPassword = passwordEncoder.encrypt(memberEditServiceDto.getPassword());
        }

        MemberEditor memberEditor = memberEditorBuilder
                .email(memberEditServiceDto.getEmail())
                .password(encryptedPassword)
                .name(memberEditServiceDto.getName())
                .phone(memberEditServiceDto.getPhone())
                .build();

        member.edit(memberEditor);
    }
    
}
