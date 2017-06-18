package com.example.teodora.myapplication.utils;

import java.util.List;

/**
 * Created by Teodora on 6/17/2017.
 */

public class Book {
    private String title;
    private String author;
    private List<String> subject;
    private int firstPublishYear;
    private String place;
    private String dateRead;
    private int status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDateRead() {
        return dateRead;
    }

    public void setDateRead(String dateRead) {
        this.dateRead = dateRead;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
