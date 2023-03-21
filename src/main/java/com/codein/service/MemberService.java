package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.domain.Session;
import com.codein.exception.AlreadyExistsAccountException;
import com.codein.exception.InvalidSigninInformation;
import com.codein.repository.MemberRepository;
import com.codein.request.PageSize;
import com.codein.request.Signin;
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


    public String signin(Signin signin) {

        Member member = memberRepository.findByEmail(signin.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        var matches = passwordEncoder.matches(signin.getPassword(), member.getPassword());
        if (!matches) {
            throw new InvalidSigninInformation();
        }

        Session session = member.addSession();
        
        return session.getAccessToken();
    }


    public void signup(Signup signup) {
        Optional<Member> memberOptional = memberRepository.findByEmail(signup.getEmail());

        if (memberOptional.isPresent()) {
            throw new AlreadyExistsAccountException();
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

}
