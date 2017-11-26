package com.cache.model;

import org.apache.commons.lang.builder.ToStringBuilder;

//学习成绩
public class Achieve {

    //排名
    private Integer rank;

    //学生
    private String student;

    //课程
    private String course;

    //成绩
    private Integer score;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
