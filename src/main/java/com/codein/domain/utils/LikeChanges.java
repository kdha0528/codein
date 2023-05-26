package com.codein.domain.utils;

import lombok.Getter;

@Getter
public class LikeChanges {
    private Integer like;
    private Integer dislike;

    public LikeChanges() {
        this.like = 0;
        this.dislike = 0;
    }
    public void increaseLike(){
        this.like++;
    }
    public void decreaseLike(){this.like--;}
    public void increaseDislike(){
        this.dislike++;
    }
    public void decreaseDislike(){
        this.dislike--;
    }
}
