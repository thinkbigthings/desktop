package org.thinkbigthings.desktop.controller;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Validator {

    public boolean isFilePresent(String filename) {

        // TODO create path if doesn't exist
        return new File(filename).exists();
    }

    public boolean isLong(String input) {
        // TODO use regex for numbers instead
        try {
            Long.parseLong(input);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }
}
