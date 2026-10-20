package com.airtribe.learntrack.entity;

public class Student extends Person {
    private String batch;
    private boolean active = true;

    public Student(int id, String firstName, String lastName, String email, String batch)
    {
        super(id,firstName,lastName,email);
        this.batch = batch;
    }

    public Student(int id, String firstName, String lastName, String batch)
    {
        this(id, firstName, lastName, null, batch);
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatch()
    {
        return this.batch;
    }

    public void setActive(boolean active)
    {
        this.active = active;   // [Claude] Uses renamed field
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public String getDisplayName() {
        String email = (getEmail() == null) ? "N/A" : getEmail();
        String status = active ? "ACTIVE" : "INACTIVE";
        return getId() + " | " + super.getDisplayName() + " | " + email + " | " + batch + " | " + status;
    }

}
