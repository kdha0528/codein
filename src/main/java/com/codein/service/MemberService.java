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
import com.codein.request.Login;
import com.codein.request.MemberEdit;
import com.codein.request.PageSize;
import com.codein.request.Signup;
import com.codein.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;


    public String login(Login login) {

        Member member = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidLoginInputException::new);

        var matches = passwordEncoder.matches(login.getPassword(), member.getPassword());
        if (!matches) {
            throw new InvalidLoginInputException();
        }

        Session session = member.addSession();

        return session.getAccessToken();
    }

    public void signup(Signup signup) {

        Optional<Member> emailOptional = memberRepository.findByEmail(signup.getEmail());
        Optional<Member> phoneOptional = memberRepository.findByPhone(signup.getPhone());

        if (emailOptional.isPresent()) {
            throw new EmailAlreadyExistsException();
        } else if (phoneOptional.isPresent()) {
            throw new PhoneAlreadyExistsException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var member = Member.builder()
                .email(signup.getEmail())
                .password(encryptedPassword)
                .name(signup.getName())
                .phone(signup.getPhone())
                .birth(signup.getBirth())
                .sex(signup.getSex())
                .build();
        memberRepository.save(member);
    }

    public List<MemberResponse> getMemberList(PageSize pageSize) {
        return memberRepository.getMemberResponseList(pageSize);
    }

    public void logout(String accessToken) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();
        member.deleteSession(session);

    }

    public void memberEdit(String accessToken, MemberEdit memberEdit) {

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(MemberNotLoginException::new);

        Member member = session.getMember();

        String encryptedPassword = null;
        MemberEditor.MemberEditorBuilder memberEditorBuilder = member.toEditor();
        if (memberEdit.getPassword() != null) {
            encryptedPassword = passwordEncoder.encrypt(memberEdit.getPassword());
        }

        MemberEditor memberEditor = memberEditorBuilder
                .email(memberEdit.getEmail())
                .password(encryptedPassword)
                .name(memberEdit.getName())
                .phone(memberEdit.getPhone())
                .build();

        member.edit(memberEditor);
    }
}
