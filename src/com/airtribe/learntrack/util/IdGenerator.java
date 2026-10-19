package com.airtribe.learntrack.util;

public class IdGenerator {
    // [Claude] Changed public -> private so counters can only be changed through the methods below (encapsulation)
    private static int sid = 1;
    private static int cid = 1;
    private static int eid = 1;

    public static int generateStudentId()
    {
        return sid++;
    }

    public static int generateCourseId()
    {
        return cid++;
    }

    public static int generateEnrollmentId()
    {
        return eid++;
    }
}
