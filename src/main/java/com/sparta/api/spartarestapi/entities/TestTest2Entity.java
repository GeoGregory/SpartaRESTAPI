package com.sparta.api.spartarestapi.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Test2", schema = "API", catalog = "")
public class TestTest2Entity {
    @Id
    @Basic
    @Column(name = "_id")
    private Integer id;
    @Basic
    @Column(name = "author")
    private String author;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "copies")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestTest2Entity that = (TestTest2Entity) o;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author) && Objects.equals(title, that.title) && Objects.equals(copies, that.copies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, copies);
    }
}
