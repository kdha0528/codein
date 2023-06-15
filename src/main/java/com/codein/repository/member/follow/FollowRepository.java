package com.codein.repository.member.follow;

import com.codein.domain.member.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
}
