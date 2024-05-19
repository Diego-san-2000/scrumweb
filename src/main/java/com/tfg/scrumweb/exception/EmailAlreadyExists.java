package com.tfg.scrumweb.exception;

public class EmailAlreadyExists extends Exception {

    public EmailAlreadyExists() {
        super();
    }
    public EmailAlreadyExists(String message){
        super(message);
    }
}
