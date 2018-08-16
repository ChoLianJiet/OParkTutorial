package com.opark.opark.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by thyemunchun on 17/10/2017.
 */

public class Author implements IUser {
    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public Author(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

   /*...*/

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
