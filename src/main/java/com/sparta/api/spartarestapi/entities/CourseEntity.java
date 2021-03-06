package com.sparta.api.spartarestapi.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document("SpartaDatabase")
public class CourseEntity {
    @Id
    private String id;
    @Field("course_id")
    private Integer courseId;
    @Field("course_name")
    private String courseName;
    private String description;
    private Boolean isActive;
    private Integer length;

    public CourseEntity() {
    }

    public CourseEntity(String id, Integer courseId, String courseName, String description, Boolean isActive, Integer length) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.isActive = isActive;
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
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
