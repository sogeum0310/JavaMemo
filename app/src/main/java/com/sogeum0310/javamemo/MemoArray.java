package com.sogeum0310.javamemo;

public class MemoArray {
    public String content;
    public int feel;
    public String date;
    public int arlam;
    public int id;
    public String arlamtime;

    public MemoArray(String content, int feel, String date, int arlam, int id, String arlamtime) {
        this.content = content;
        this.feel = feel;
        this.date = date;
        this.arlam = arlam;
        this.id = id;
        this.arlamtime = arlamtime;
    }

    public MemoArray() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFeel() {
        return feel;
    }

    public void setFeel(int feel) {
        this.feel = feel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getArlam() {
        return arlam;
    }

    public void setArlam(int arlam) {
        this.arlam = arlam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArlamtime() {
        return arlamtime;
    }

    public void setArlamtime(String arlamtime) {
        this.arlamtime = arlamtime;
    }
}
