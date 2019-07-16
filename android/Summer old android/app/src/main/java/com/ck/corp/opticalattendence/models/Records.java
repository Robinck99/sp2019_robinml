package com.ck.corp.opticalattendence.models;

public class Records {

    private String name;
    private String rollno;
    private String attend;

    public Records(String name, String rollno, String attend) {
        this.name = name;
        this.rollno = rollno;
        this.attend = attend;
    }

    public Records(String name, String rollno) {
        this.name = name;
        this.rollno = rollno;
        attend = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }
}
