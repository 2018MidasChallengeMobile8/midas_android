package com.xema.midas.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xema0 on 2018-05-20.
 */

public class Profile {
    private String name;
    @SerializedName("photo_url")
    private String profileImage;
    private String comment;
    private int uid;//user pk

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
