package com.sparta.api.spartarestapi.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Test3", schema = "API")
public class SpartanEntity {
    @Id
    @Basic
    @Column(name = "_id")
    private String id;
    @Basic
    @Column(name = "courseEndDate")
    private LocalDate courseEndDate;
    @Basic
    @Column(name = "courseStartDate")
    private LocalDate courseStartDate;
    @Basic
    @Column(name = "course_id")
    private String courseId;
    @Basic
    @Column(name = "firstname")
    private String firstname;
    @Basic
    @Column(name = "lastName")
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(LocalDate courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public LocalDate getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(LocalDate courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
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
