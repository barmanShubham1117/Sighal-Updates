package com.skilligence.myapplication.model;

import java.util.ArrayList;

public class BlogModel {
    private String blogId, title, description, imgURL;
    private int likes;
    private ArrayList<Comments> comment = new ArrayList<>();

    public BlogModel() {
        this.likes = 0;
    }

    public BlogModel(String blogId, String title, String description, String imgURL, int likes, ArrayList<Comments> comment) {
        this.blogId = blogId;
        this.title = title;
        this.description = description;
        this.imgURL = imgURL;
        this.likes = likes;
        this.comment = comment;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<Comments> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comments> comment) {
        this.comment = comment;
    }

    class Comments {
        private String userKey, author, comment, time;

        public Comments() {
        }

        public Comments(String userKey, String author, String comment, String time) {
            this.userKey = userKey;
            this.author = author;
            this.comment = comment;
            this.time = time;
        }

        public String getUserKey() {
            return userKey;
        }

        public void setUserKey(String userKey) {
            this.userKey = userKey;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
