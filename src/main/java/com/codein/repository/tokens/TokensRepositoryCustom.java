package com.codein.repository.tokens;

import com.codein.domain.auth.Tokens;

import java.util.Optional;

public interface TokensRepositoryCustom {


    Tokens findByRefreshToken(String refreshToken);
}
