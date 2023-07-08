package com.codein.repository.tokens;

import com.codein.domain.auth.Tokens;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.auth.QTokens.tokens;

@Repository
@RequiredArgsConstructor
public class TokensRepositoryCustomImpl implements TokensRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Tokens findByRefreshToken(String refreshToken) {
        return jpaQueryFactory.selectFrom(tokens)
                .where(tokens.refreshToken.eq(refreshToken))
                .fetchOne();
    }
}
