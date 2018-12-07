package com.developer.athirah.sukarela.models;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class ModelUser {

    private String uid;
    private String name;
    private String email;
    private Integer age;


    public ModelUser() {
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
