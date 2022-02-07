package com.sparta.api.spartarestapi.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Test3", schema = "API")
public class CourseEntity {
    @Id
    @Basic
    @Column(name = "course_id")
    private String courseId;
    @Basic
    @Column(name = "course_name")
    private String courseName;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "isActive")
    private Boolean isActive;
    @Basic
    @Column(name = "length")
    private Integer length;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(courseName, that.courseName) && Objects.equals(description, that.description) && Objects.equals(isActive, that.isActive) && Objects.equals(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, description, isActive, length);
    }
}
