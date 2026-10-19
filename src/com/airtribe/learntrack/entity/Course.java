package com.airtribe.learntrack.entity;

public class Course {
    private int id;
    private String courseName;
    private String description;
    private double durationInWeeks;
    private boolean active;

    public Course(int id, String courseName, String description, double durationInWeeks)
    {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.durationInWeeks = durationInWeeks;
        this.active = true;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDurationInWeeks(double durationInWeeks)
    {
        this.durationInWeeks = durationInWeeks;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public int getId(){ return this.id; }

    public String getCourseName(){
        return this.courseName;
    }

    public String getDescription(){
        return this.description;
    }

    public double getDurationInWeeks(){
        return this.durationInWeeks;
    }

    public boolean isActive(){
        return this.active;
    }
}
