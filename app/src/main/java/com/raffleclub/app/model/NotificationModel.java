package com.raffleclub.app.model;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("zero")
    public String zero;

    @SerializedName("one")
    public String one;

    @SerializedName("two")
    public String two;

    @SerializedName("three")
    public String three;

    @SerializedName("four")
    public String four;

    @SerializedName("title")
    public String title;

    @SerializedName("id")
    public String id;

    @SerializedName("image")
    public String image;

    @SerializedName("message")
    public String message;

    @SerializedName("seen")
    public int seen;

    @SerializedName("url")
    public String url;

    @SerializedName("created")
    public String created;

    @SerializedName("elapsed_time")
    public String elapsed_time;

    public String getElapsed_time() {
        return elapsed_time;
    }

    public int getSeen() {
        return seen;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    public String getZero() {
        return zero;
    }

    public void setZero(String zero) {
        this.zero = zero;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
