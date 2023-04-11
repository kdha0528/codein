package com.codein.repository.profileimage;

import com.codein.domain.image.ProfileImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.image.QProfileImage.profileImage;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryCustomImpl implements ProfileImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProfileImage findByName(String name) {
        return jpaQueryFactory.selectFrom(profileImage)
                .where(profileImage.imgFileName.eq(name))
                .fetchOne();
    }
}
