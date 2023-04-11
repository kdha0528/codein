package com.codein.repository.profileimage;

import com.codein.domain.image.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long>, ProfileImageRepositoryCustom {
}
