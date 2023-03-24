package com.codein.repository;

import com.codein.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long>, MemberRepositoryCustom {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findByEmailAndPassword(String email, String password);

}
