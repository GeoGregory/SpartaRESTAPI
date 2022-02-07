package com.sparta.api.spartarestapi.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document("Test2")
public class TestTest2Entity {
    @Id
    private Integer id;
    private String author;
    private String title;
    private Integer copies;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

}
