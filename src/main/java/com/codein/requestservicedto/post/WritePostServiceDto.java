package com.codein.requestservicedto.post;

import com.codein.domain.member.Member;
import com.codein.domain.post.Category;
import com.codein.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WritePostServiceDto {
    private final String category;
    private final String title;
    private final String content;

    @Builder
    public WritePostServiceDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .category(Category.valueOf(this.category))
                .title(this.title)
                .content(this.content)
                .build();

    }
}
