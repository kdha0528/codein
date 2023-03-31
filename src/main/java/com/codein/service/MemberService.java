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
import com.codein.requestservicedto.EditMemberServiceDto;
import com.codein.requestservicedto.LoginServiceDto;
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


    public List<MemberResponseDto> getMemberList(PageSizeDto pageSizeDto) {
        return memberRepository.getMemberResponseList(pageSizeDto);
    }

    @Transactional
    public void logout(String accessToken) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        sessionRepository.delete(session);
        Member nullMember = memberRepository.findByAccessToken(accessToken);
        if (nullMember != null) System.out.println("member is not null = " + nullMember.getEmail());

    }

    @Transactional
    public void editMember(String accessToken, EditMemberServiceDto editMemberServiceDto) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        String encryptedPassword = null;
        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        if (editMemberServiceDto.getPassword() != null) {
            encryptedPassword = passwordEncoder.encrypt(editMemberServiceDto.getPassword());
        }
        MemberEditor memberEditor = memberEditorBuilder
                .email(editMemberServiceDto.getEmail())
                .password(encryptedPassword)
                .name(editMemberServiceDto.getName())
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
