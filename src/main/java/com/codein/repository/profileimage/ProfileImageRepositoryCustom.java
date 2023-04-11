package com.codein.repository.profileimage;

import com.codein.domain.image.ProfileImage;

public interface ProfileImageRepositoryCustom {

    ProfileImage findByName(String name);
}
