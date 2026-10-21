package com.hr.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Table(name="CREATE_POST")
@Entity
public class CreatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name="COMMENT",length = 2000)
    @NotNull
    private String  comment;

    private String addedDate;

    public CreatePost() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public String toString() {
        return "CreatePost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", addedDate='" + addedDate + '\'' +
                '}';
    }
}
