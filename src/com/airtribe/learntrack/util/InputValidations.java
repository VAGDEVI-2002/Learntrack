package com.airtribe.learntrack.util;
import com.airtribe.learntrack.exception.InvalidInputException;

public class InputValidations {
    public static boolean validateEmail(String email)
    {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static void checkNoEmpty(String str) throws InvalidInputException
    {
        if(str==null || str.trim().isEmpty())
        {
            throw new InvalidInputException("Cannot be empty");
        }
    }
}
