package com.airtribe.learntrack.entity;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private String enrollmentDate;
    private EnrollmentStatus status;   // [Claude] Changed String -> EnrollmentStatus

    public Enrollment(int id, int sid, int cid, String enrollmentDate, EnrollmentStatus status)
    {
        this.id = id;
        this.studentId = sid;
        this.courseId = cid;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setStudentId(int sid)
    {
        this.studentId = sid;
    }

    public int getStudentId()
    {
        return this.studentId;
    }

    public void setCourseId(int cid)
    {
        this.courseId = cid;
    }

    public int getCourseId()   // [Claude] Renamed from getcourseId to follow getter naming
    {
        return this.courseId;
    }

    public void setEnrollmentDate(String enrollmentDate)
    {
        this.enrollmentDate = enrollmentDate;
    }

    public String getEnrollmentDate()
    {
        return this.enrollmentDate;
    }

    public void setStatus(EnrollmentStatus status)
    {
        this.status = status;
    }

    public EnrollmentStatus getStatus()
    {
        return this.status;
    }
}
