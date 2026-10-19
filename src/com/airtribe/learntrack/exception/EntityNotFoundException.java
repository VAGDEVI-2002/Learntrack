// [Claude] New file: custom exception thrown when a Student (or Course) with a given id does not exist
package com.airtribe.learntrack.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message)
    {
        super(message);
    }
}
