package com.teamresourceful.resourcefulsettings.versioning;

public class InvalidVersionFormatException extends Exception {

    private static final String MESSAGE = "version.properties file missing in root project directory!";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
