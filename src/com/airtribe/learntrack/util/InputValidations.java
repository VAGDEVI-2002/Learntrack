package com.airtribe.learntrack.util;
import com.airtribe.learntrack.exception.InvalidInputException;

public class InputValidations {
    public static boolean validateEmail(String email)
    {
        // text before '@', and a domain like "example.com" after it (no spaces, no empty parts)
        return email != null && email.matches("[^@\\s]+@[^@\\s.]+(\\.[^@\\s.]+)+");
    }

    public static void checkNoEmpty(String str) throws InvalidInputException
    {
        if(str==null || str.trim().isEmpty())
        {
            throw new InvalidInputException("Cannot be empty");
        }
    }
}
