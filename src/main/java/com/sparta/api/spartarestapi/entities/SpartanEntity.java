package com.sparta.api.spartarestapi.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDate;
import java.util.Objects;

@Document("SpartanDB")
public class SpartanEntity {
    @Id
    private String id;
    private String courseEndDate;
    private String courseStartDate;
    @Field("course_id")
    private Integer courseId;
    private String firstname;
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpartanEntity that = (SpartanEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(courseEndDate, that.courseEndDate) && Objects.equals(courseStartDate, that.courseStartDate) && Objects.equals(courseId, that.courseId) && Objects.equals(firstname, that.firstname) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseEndDate, courseStartDate, courseId, firstname, lastName);
    }
}
