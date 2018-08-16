package com.opark.opark.model;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

/**
 * Created by thyemunchun on 17/10/2017.
 */

public class Message implements IMessage {
    private String id;
    private String text;
    private Author author;
    private Date createdAt;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;

    public Message(String id, Author user, String text) {
        this(id, user, text, new Date());
    }

    public Message(String id, Author author, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }

   /*...*/

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }
}